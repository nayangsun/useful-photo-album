/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.useful_photo_album.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.useful_photo_album.data.signin.AuthenticatedUserInfo
import com.example.useful_photo_album.domain.feed.ConferenceState
import com.example.useful_photo_album.domain.feed.ConferenceState.UPCOMING
import com.example.useful_photo_album.domain.feed.ConferenceState.ENDED
import com.example.useful_photo_album.domain.feed.GetConferenceStateUseCase
import com.example.useful_photo_album.domain.feed.LoadAnnouncementsUseCase
import com.example.useful_photo_album.domain.feed.LoadCurrentMomentUseCase
import com.example.useful_photo_album.domain.sessions.LoadStarredAndReservedSessionsUseCase
import com.example.useful_photo_album.domain.settings.GetTimeZoneUseCase
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.feed.*
import com.example.useful_photo_album.shared.analytics.AnalyticsActions
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.example.useful_photo_album.shared.di.MapFeatureEnabledFlag
import com.example.useful_photo_album.shared.di.ReservationEnabledFlag
import com.example.useful_photo_album.shared.model.temp.Announcement
import com.example.useful_photo_album.shared.model.temp.Moment
import com.example.useful_photo_album.shared.model.temp.Session
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.successOr
import com.example.useful_photo_album.shared.time.TimeProvider
import com.example.useful_photo_album.shared.util.TimeUtils
import com.example.useful_photo_album.shared.util.TimeUtils.ConferenceDays
import com.example.useful_photo_album.shared.util.toEpochMilli
import com.example.useful_photo_album.shared.util.tryOffer
import com.example.useful_photo_album.presentation.messages.SnackbarMessage
import com.example.useful_photo_album.presentation.messages.SnackbarMessageManager
import com.example.useful_photo_album.presentation.sessioncommon.OnSessionClickListener
import com.example.useful_photo_album.presentation.sessioncommon.OnSessionStarClickListener
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.presentation.theme.ThemedActivityDelegate
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

/**
 * Loads data and exposes it to the view.
 * By annotating the constructor with [@Inject], Dagger will use that constructor when needing to
 * create the object, so defining a [@Provides] method for this class won't be needed.
 */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadCurrentMomentUseCase: LoadCurrentMomentUseCase,
    loadAnnouncementsUseCase: LoadAnnouncementsUseCase,
    private val loadStarredAndReservedSessionsUseCase: LoadStarredAndReservedSessionsUseCase,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    getConferenceStateUseCase: GetConferenceStateUseCase,
    private val timeProvider: TimeProvider,
    private val analyticsHelper: AnalyticsHelper,
    private val signInViewModelDelegate: SignInViewModelDelegate,
    themedActivityDelegate: ThemedActivityDelegate,
    private val snackbarMessageManager: SnackbarMessageManager
) : ViewModel(),
    FeedEventListener,
    ThemedActivityDelegate by themedActivityDelegate,
    SignInViewModelDelegate by signInViewModelDelegate {

    companion object {
        // Show at max 10 sessions in the horizontal sessions list as user can click on
        // View All sessions and go to schedule to view the full list
        private const val MAX_SESSIONS = 10

        // Indicates there is no header to show at the current time.
        private object NoHeader

        // Indicates there is no sessions related display on the home screen as the conference is
        // over.
        private object NoSessionsContainer
    }

    @Inject
    @JvmField
    @ReservationEnabledFlag
    var isReservationEnabledByRemoteConfig: Boolean = false

    @Inject
    @JvmField
    @MapFeatureEnabledFlag
    var isMapEnabledByRemoteConfig: Boolean = false

    // Exposed to the view as a StateFlow but it's a one-shot operation.
    val timeZoneId = flow<ZoneId> {
        if (getTimeZoneUseCase(Unit).successOr(true)) {
            emit(TimeUtils.CONFERENCE_TIMEZONE)
        } else {
            emit(ZoneId.systemDefault())
        }
    }.stateIn(viewModelScope, Eagerly, TimeUtils.CONFERENCE_TIMEZONE)

    private val loadSessionsResult: StateFlow<Result<List<UserSession>>> =
        signInViewModelDelegate.userId
            .flatMapLatest {
                // TODO(jdkoren): might need to show sessions for not signed in users too...
                loadStarredAndReservedSessionsUseCase(it)
            }
            .stateIn(viewModelScope, WhileViewSubscribed, Result.Loading)

    private val conferenceState: StateFlow<ConferenceState> = getConferenceStateUseCase(Unit)
        .onEach {
            if (it is Result.Error) {
                snackbarMessageManager.addMessage(SnackbarMessage(R.string.feed_loading_error))
            }
        }
        .map { it.successOr(UPCOMING) }
        .stateIn(viewModelScope, WhileViewSubscribed, UPCOMING)

    // SIDE EFFECTS: Navigation actions
    private val _navigationActions = Channel<FeedNavigationAction>(capacity = Channel.CONFLATED)
    // Exposed with receiveAsFlow to make sure that only one observer receives updates.
    val navigationActions = _navigationActions.receiveAsFlow()

    private val currentMomentResult: StateFlow<Result<Moment?>> = conferenceState.map {
        // Reload if conferenceState changes
        loadCurrentMomentUseCase(timeProvider.now())
    }.stateIn(viewModelScope, WhileViewSubscribed, Result.Loading)

    private val loadAnnouncementsResult: StateFlow<Result<List<Announcement>>> = flow {
        emit(loadAnnouncementsUseCase(timeProvider.now()))
    }.onEach {
        if (it is Result.Error) {
            snackbarMessageManager.addMessage(SnackbarMessage(R.string.feed_loading_error))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, Result.Loading)

    private val announcementsPreview: StateFlow<List<Any>> = loadAnnouncementsResult.map {
        val announcementsHeader = AnnouncementsHeader(
            showPastNotificationsButton = it.successOr(emptyList()).size > 1
        )
        if (it is Result.Loading) {
            listOf(announcementsHeader, LoadingIndicator)
        } else {
            listOf(
                announcementsHeader,
                it.successOr(emptyList()).firstOrNull() ?: AnnouncementsEmpty
            )
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private val feedSessionsContainer: Flow<FeedSessions> = loadSessionsResult
        .combine(timeZoneId) { sessions, timeZone ->
            createFeedSessionsContainer(sessions, timeZone)
        }

    private val sessionContainer: StateFlow<Any> =
        combine(
            feedSessionsContainer,
            conferenceState,
            signInViewModelDelegate.userInfo
        ) { sessionsContainer: FeedSessions,
            conferenceState: ConferenceState,
            userInfo: AuthenticatedUserInfo?
            ->
            val isSignedIn = userInfo?.isSignedIn() ?: false
            val isRegistered = userInfo?.isRegistered() ?: false
            if (conferenceState != ENDED && isSignedIn && isRegistered &&
                isReservationEnabledByRemoteConfig
            ) {
                sessionsContainer
            } else {
                NoSessionsContainer
            }
        }.stateIn(viewModelScope, WhileViewSubscribed, NoSessionsContainer)

    private val currentFeedHeader: StateFlow<Any> = conferenceState.combine(
        currentMomentResult
    ) { conferenceStarted: ConferenceState, momentResult ->
        if (conferenceStarted == UPCOMING) {
            CountdownItem
        } else {
            // Use case can return null even on success, so replace nulls with a sentinel
            momentResult.successOr(null) ?: NoHeader
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, NoHeader)

    // TODO: Replace Any with something meaningful.
    val feed: StateFlow<List<Any>> = combine(
        currentFeedHeader,
        sessionContainer,
        announcementsPreview
    ) { feedHeader: Any, sessionContainer: Any, announcementsPreview: List<Any> ->
        val feedItems = mutableListOf<Any>()
        if (feedHeader != NoHeader) {
            feedItems.add(feedHeader)
        }
        if (sessionContainer != NoSessionsContainer) {
            feedItems.add(sessionContainer)
        }
        feedItems
            .plus(announcementsPreview)
            .plus(FeedSustainabilitySection)
            .plus(FeedSocialChannelsSection)
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private fun createFeedSessionsContainer(
        sessionsResult: Result<List<UserSession>>,
        timeZoneId: ZoneId
    ): FeedSessions {
        val sessions = sessionsResult.successOr(emptyList())
        val now = ZonedDateTime.ofInstant(timeProvider.now(), timeZoneId)

        // TODO: Making conferenceState a sealed class and moving currentDay in STARTED
        //  state might be a better option
        val currentDayEndTime = TimeUtils.getCurrentConferenceDay()?.end
            // Treat start of the conference as endTime as sessions shouldn't be shown if the
            // currentConferenceDay is null
            ?: ConferenceDays.first().start

        val upcomingReservedSessions = sessions
            .filter {
                it.userEvent.isReserved() &&
                    it.session.endTime.isAfter(now) &&
                    it.session.endTime.isBefore(currentDayEndTime)
            }
            .take(MAX_SESSIONS)

        val titleId = R.string.feed_sessions_title
        val actionId = R.string.feed_view_full_schedule

        return FeedSessions(
            titleId = titleId,
            actionTextId = actionId,
            userSessions = upcomingReservedSessions,
            timeZoneId = timeZoneId,
            isLoading = sessionsResult is Result.Loading,
            isMapFeatureEnabled = isMapEnabledByRemoteConfig
        )
    }

    override fun openEventDetail(id: SessionId) {
        analyticsHelper.logUiEvent(
            "Home to event detail",
            AnalyticsActions.HOME_TO_SESSION_DETAIL
        )
        _navigationActions.tryOffer(FeedNavigationAction.NavigateToSession(id))
    }

    override fun openSchedule(showOnlyPinnedSessions: Boolean) {
        analyticsHelper.logUiEvent("Home to Schedule", AnalyticsActions.HOME_TO_SCHEDULE)
        _navigationActions.tryOffer(FeedNavigationAction.NavigateToScheduleAction)
    }

    override fun onStarClicked(userSession: UserSession) {
        TODO("not implemented")
    }

    override fun signIn() {
        analyticsHelper.logUiEvent("Home to Sign In", AnalyticsActions.HOME_TO_SIGN_IN)
        _navigationActions.tryOffer(FeedNavigationAction.OpenSignInDialogAction)
    }

    override fun openMap(moment: Moment) {
        analyticsHelper.logUiEvent(moment.title.toString(), AnalyticsActions.HOME_TO_MAP)
        _navigationActions.tryOffer(
            FeedNavigationAction.NavigateAction(
                FeedFragmentDirections.toMap(
                    featureId = moment.featureId,
                    startTime = moment.startTime.toEpochMilli()
                )
            )
        )
    }

    override fun openLiveStream(liveStreamUrl: String) {
        analyticsHelper.logUiEvent(liveStreamUrl, AnalyticsActions.HOME_TO_LIVESTREAM)
        _navigationActions.tryOffer(FeedNavigationAction.OpenLiveStreamAction(liveStreamUrl))
    }

    override fun openMapForSession(session: Session) {
        analyticsHelper.logUiEvent(session.id, AnalyticsActions.HOME_TO_MAP)
        val directions = FeedFragmentDirections.toMap(
            featureId = session.room?.id,
            startTime = session.startTime.toEpochMilli()
        )

        _navigationActions.tryOffer(FeedNavigationAction.NavigateAction(directions))
    }

    override fun openPastAnnouncements() {
        analyticsHelper.logUiEvent("", AnalyticsActions.HOME_TO_ANNOUNCEMENTS)
        val directions = FeedFragmentDirections.toAnnouncementsFragment()
        _navigationActions.tryOffer(FeedNavigationAction.NavigateAction(directions))
    }
}

interface FeedEventListener : OnSessionClickListener, OnSessionStarClickListener {
    fun openSchedule(showOnlyPinnedSessions: Boolean)
    fun signIn()
    fun openMap(moment: Moment)
    fun openLiveStream(liveStreamUrl: String)
    fun openMapForSession(session: Session)
    fun openPastAnnouncements()
}

sealed class FeedNavigationAction {
    class NavigateToSession(val sessionId: SessionId) : FeedNavigationAction()
    class NavigateAction(val directions: NavDirections) : FeedNavigationAction()
    object OpenSignInDialogAction : FeedNavigationAction()
    class OpenLiveStreamAction(val url: String) : FeedNavigationAction()
    object NavigateToScheduleAction : FeedNavigationAction()
}
