package com.example.useful_photo_album.domain.prefs

import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.UseCase
import com.example.useful_photo_album.domain.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


open class AccessTokenPrefSaveActionUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<String, String>(dispatcher) {
    override suspend fun execute(parameters: String): String {
        preferenceStorage.getAccessToken(parameters)
        return parameters
    }
}
