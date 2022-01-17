package com.example.useful_photo_album.data.unsplash

import com.example.useful_photo_album.data.api.UnsplashApi
import com.example.useful_photo_album.shared.model.unsplash.UnsplashPhoto
import com.example.useful_photo_album.shared.model.unsplash.UnsplashSearchResponse
import javax.inject.Inject

internal class UnsplashRepositoryImpl @Inject constructor(
    private val service: UnsplashApi,
) : UnsplashRepository {

    override suspend fun getSearchPhotos(query: String, page: Int, perPage: Int): UnsplashSearchResponse {
        return service.searchPhotos(query, page, perPage)
    }

    override suspend fun getRandomPhotos(count: Int): List<UnsplashPhoto> {
        return service.randomPhotos(count)
    }

    companion object {
        const val NETWORK_COUNT = 30
        private const val refreshIntervalMs: Long = 5000
        private const val NETWORK_PAGE_SIZE = 25
    }
}