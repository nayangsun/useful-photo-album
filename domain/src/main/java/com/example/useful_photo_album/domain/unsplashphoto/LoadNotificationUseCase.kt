package com.example.useful_photo_album.domain.unsplashphoto

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.useful_photo_album.domain.PagingUseCase
import com.example.useful_photo_album.domain.pagingsource.PhotoPagingSource
import com.example.useful_photo_album.domain.repository.UnsplashRepository
import com.example.useful_photo_album.shared.model.UnsplashPhoto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadPhotoUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : PagingUseCase<NotificationParameters, Flow<PagingData<UnsplashPhoto>>>() {

    override fun execute(parameters: NotificationParameters): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { PhotoPagingSource(unsplashRepository, parameters.type) }
        ).flow
    }

    companion object {

        private const val NETWORK_PAGE_SIZE = 10
    }
}

data class NotificationParameters(
    val type: PhotoPagingSource.QueryType,
)