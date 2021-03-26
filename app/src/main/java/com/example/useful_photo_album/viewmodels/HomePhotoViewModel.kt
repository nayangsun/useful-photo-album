package com.example.useful_photo_album.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.useful_photo_album.HomePhotoFragment
import com.example.useful_photo_album.data.PhotoWriteRepository
import com.example.useful_photo_album.data.model.PhotoWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * The ViewModel for [HomePhotoFragment].
 */
@HiltViewModel
class HomePhotoViewModel @Inject internal constructor(
    photoWriteRepository: PhotoWriteRepository
) : ViewModel() {
    val photoWritings: LiveData<List<PhotoWrite>> =
        photoWriteRepository.getPhotoWrites().asLiveData()
}