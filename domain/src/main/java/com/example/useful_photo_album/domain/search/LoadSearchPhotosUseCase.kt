package com.example.useful_photo_album.domain.search

import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.FlowUseCase
import com.example.useful_photo_album.domain.repository.UnsplashRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadSearchPhotosUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, UserInfo>(dispatcher) {
    override fun execute(parameters: QuerySize): Flow<Result<UserInfo>> {
        return farmsRepository.getFarms(parameters).map { result ->
            when (result) {
                is Result.Success -> {
                    val farmInfoList = result.data
                    Result.Success(UserInfo.fromFarmList(farmInfoList))
                }
                is Result.Error -> {
                    Result.Error(result.exception)
                }
                is Result.Loading -> Result.Loading
            }
        }
    }
}


