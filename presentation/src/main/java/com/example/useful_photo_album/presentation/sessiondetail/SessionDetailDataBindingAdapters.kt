/*
 * Copyright 2018 Google LLC
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

package com.example.useful_photo_album.presentation.sessiondetail

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.reservation.ReservationViewState
import com.example.useful_photo_album.presentation.reservation.ReservationViewState.RESERVABLE
import com.example.useful_photo_album.presentation.reservation.StarReserveFab
import com.example.useful_photo_album.shared.model.temp.Session
import com.example.useful_photo_album.shared.model.temp.SessionType.AFTER_DARK
import com.example.useful_photo_album.shared.model.temp.SessionType.APP_REVIEW
import com.example.useful_photo_album.shared.model.temp.SessionType.GAME_REVIEW
import com.example.useful_photo_album.shared.model.temp.SessionType.KEYNOTE
import com.example.useful_photo_album.shared.model.temp.SessionType.OFFICE_HOURS
import com.example.useful_photo_album.shared.model.temp.SessionType.SESSION
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.util.TimeUtils
import org.threeten.bp.Duration
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

/* Narrow headers used for events that have neither a photo nor video url. */
@BindingAdapter("eventNarrowHeader")
fun eventNarrowHeaderImage(imageView: ImageView, session: Session?) {
    session ?: return

    val resId = when (session.type) {
        KEYNOTE -> R.drawable.event_narrow_keynote
        // For the next few types, we choose a random image, but we should use the same image for a
        // given event, so use the id to pick.
        SESSION -> when (session.id.hashCode() % 4) {
            0 -> R.drawable.event_narrow_session1
            1 -> R.drawable.event_narrow_session2
            2 -> R.drawable.event_narrow_session3
            else -> R.drawable.event_narrow_session4
        }
        OFFICE_HOURS -> when (session.id.hashCode() % 3) {
            0 -> R.drawable.event_narrow_office_hours1
            1 -> R.drawable.event_narrow_office_hours2
            else -> R.drawable.event_narrow_office_hours3
        }
        APP_REVIEW -> when (session.id.hashCode() % 3) {
            0 -> R.drawable.event_narrow_app_reviews1
            1 -> R.drawable.event_narrow_app_reviews2
            else -> R.drawable.event_narrow_app_reviews3
        }
        GAME_REVIEW -> when (session.id.hashCode() % 3) {
            0 -> R.drawable.event_narrow_game_reviews1
            1 -> R.drawable.event_narrow_game_reviews2
            else -> R.drawable.event_narrow_game_reviews3
        }
        AFTER_DARK -> R.drawable.event_narrow_afterhours
        else -> R.drawable.event_narrow_other
    }
    imageView.setImageResource(resId)
}

/* Photos are used if the event has a photo and/or a video url. */
@BindingAdapter("eventPhoto")
fun eventPhoto(imageView: ImageView, session: Session?) {
    session ?: return

    val resId = when (session.type) {
        KEYNOTE -> R.drawable.event_placeholder_keynote
        // Choose a random image, but we should use the same image for a given session, so use ID to
        // pick.
        SESSION -> when (session.id.hashCode() % 4) {
            0 -> R.drawable.event_placeholder_session1
            1 -> R.drawable.event_placeholder_session2
            2 -> R.drawable.event_placeholder_session3
            else -> R.drawable.event_placeholder_session4
        }
        // Other event types probably won't have photos or video, but just in case...
        else -> R.drawable.event_placeholder_keynote
    }

    if (session.hasPhoto) {
        Glide.with(imageView)
            .load(session.photoUrl)
            .apply(RequestOptions().placeholder(resId))
            .into(imageView)
    } else {
        imageView.setImageResource(resId)
    }
}

@BindingAdapter(
    value = ["sessionDetailStartTime", "sessionDetailEndTime", "timeZoneId"], requireAll = true
)
fun timeString(
    view: TextView,
    sessionDetailStartTime: ZonedDateTime?,
    sessionDetailEndTime: ZonedDateTime?,
    timeZoneId: ZoneId?
) {
    if (sessionDetailStartTime == null || sessionDetailEndTime == null || timeZoneId == null) {
        view.text = ""
    } else {
        view.text = TimeUtils.timeString(
            TimeUtils.zonedTime(sessionDetailStartTime, timeZoneId),
            TimeUtils.zonedTime(sessionDetailEndTime, timeZoneId)
        )
    }
}

@BindingAdapter("sessionStartCountdown")
fun sessionStartCountdown(view: TextView, timeUntilStart: Duration?) {
    if (timeUntilStart == null) {
        view.visibility = GONE
    } else {
        view.visibility = VISIBLE
        val minutes = timeUntilStart.toMinutes()
        view.text = view.context.resources.getQuantityString(
            R.plurals.session_starting_in, minutes.toInt(), minutes.toString()
        )
    }
}

@BindingAdapter(
    "userSession",
    "isSignedIn",
    "isRegistered",
    "isReservable",
    "isReservationDeniedByCutoff",
    "eventListener",
    requireAll = true
)
fun assignFab(
    fab: StarReserveFab,
    userSession: UserSession?,
    isSignedIn: Boolean,
    isRegistered: Boolean,
    isReservable: Boolean,
    isReservationDeniedByCutoff: Boolean,
    eventListener: SessionDetailEventListener
) {
    userSession ?: return
    when {
        !isSignedIn -> {
            if (isReservable) {
                fab.reservationStatus = RESERVABLE
            } else {
                fab.isChecked = false
            }
            fab.setOnClickListener { eventListener.onLoginClicked() }
        }
        isRegistered && isReservable -> {
            fab.reservationStatus = ReservationViewState.fromUserEvent(
                userSession.userEvent,
                isReservationDeniedByCutoff
            )
            fab.setOnClickListener { eventListener.onReservationClicked() }
        }
        else -> {
            fab.isChecked = userSession.userEvent.isStarred
            fab.setOnClickListener { eventListener.onStarClicked(userSession) }
        }
    }
}
