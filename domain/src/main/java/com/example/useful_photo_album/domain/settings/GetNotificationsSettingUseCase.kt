package com.example.useful_photo_album.domain.settings

import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.UseCase
import com.example.useful_photo_album.domain.data.spec.pref.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetNotificationsSettingUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, Boolean>(dispatcher) {
    // TODO use preferToReceiveNotifications as flow
    override suspend fun execute(parameters: Unit) = false
}
