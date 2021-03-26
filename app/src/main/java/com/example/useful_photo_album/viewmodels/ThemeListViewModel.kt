package com.example.useful_photo_album.viewmodels

import androidx.lifecycle.*
import com.example.useful_photo_album.SelectPhotoFragment
import com.example.useful_photo_album.data.PhotoThemeRepository
import com.example.useful_photo_album.data.model.PhotoTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for [SelectPhotoFragment].
 */
@HiltViewModel
class ThemeListViewModel @Inject internal constructor(
    themeRepository: PhotoThemeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val favoriteNumber: MutableStateFlow<Int> = MutableStateFlow(
        savedStateHandle.get(FAVORITE_THEME_SAVED_STATE_KEY) ?: NO_FAVORITE
    )

    val themes: LiveData<List<PhotoTheme>> = favoriteNumber.flatMapLatest { favorite ->
        if (favorite == NO_FAVORITE) {
            themeRepository.getThemes()
        } else {
            themeRepository.getThemesWithFavoriteNumber(favorite)
        }
    }.asLiveData()

    init {
        viewModelScope.launch {
            favoriteNumber.collect { newFavorite ->
                savedStateHandle.set(FAVORITE_THEME_SAVED_STATE_KEY, newFavorite)
            }
        }
    }

    fun setFavoriteNumber(num: Int) {
        favoriteNumber.value = num
    }

    fun clearFavoriteNumber() {
        favoriteNumber.value = NO_FAVORITE
    }

    fun isFiltered() = favoriteNumber.value != NO_FAVORITE

    companion object {
        private const val NO_FAVORITE = -1
        private const val FAVORITE_THEME_SAVED_STATE_KEY = "FAVORITE_THEME_SAVED_STATE_KEY"
    }
}
