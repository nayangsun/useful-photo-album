package com.example.useful_photo_album.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.data.PhotoWriteRepository
import com.example.useful_photo_album.WriteFragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * The ViewModel used in [WriteFragment].
 */
class WriteViewModel @AssistedInject internal constructor(
    private val photoWriteRepository: PhotoWriteRepository,
    @Assisted private val photoId: String
) : ViewModel() {

    fun addWritePhoto(title: String, context: String, imageUrl: String?) {
        imageUrl?.let {
            viewModelScope.launch {
                photoWriteRepository.createPhotoWrite(photoId, title, context, imageUrl)
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: WriteViewModelFactory,
            photoId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(photoId) as T
            }
        }
    }
}

@AssistedFactory
interface WriteViewModelFactory {
    fun create(photoId: String): WriteViewModel
}

