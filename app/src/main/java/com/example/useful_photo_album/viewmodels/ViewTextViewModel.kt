package com.example.useful_photo_album.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.ViewTextFragment
import com.example.useful_photo_album.data.PhotoWriteRepository
import com.example.useful_photo_album.data.model.PhotoWrite
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * The ViewModel used in [ViewTextFragment].
 */
class ViewTextViewModel @AssistedInject internal constructor(
    private val photoWriteRepository: PhotoWriteRepository,
    @Assisted private val photoId: String
) : ViewModel() {

    val photo = photoWriteRepository.getPhotoWrite(photoId).asLiveData()

    fun deletePhotoWrite(photo: PhotoWrite) {
        viewModelScope.launch {
            photoWriteRepository.removePhotoWrite(photo)
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: ViewTextViewModelFactory,
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
interface ViewTextViewModelFactory {
    fun create(photoId: String): ViewTextViewModel
}
