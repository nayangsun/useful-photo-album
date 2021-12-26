package com.example.useful_photo_album.prefs

import com.farmsplan.app.common.prefs.PreferenceStorage
import com.farmsplan.app.di.IoDispatcher
import com.farmsplan.app.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class AccessTokenPrefSaveActionUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<String, String>(dispatcher) {
    override suspend fun execute(parameters: String): String {
        preferenceStorage.getAuthToken(parameters)
        return parameters
    }
}
