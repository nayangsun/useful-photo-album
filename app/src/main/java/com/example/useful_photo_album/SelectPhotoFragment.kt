package com.example.useful_photo_album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.useful_photo_album.adapters.ThemeAdapter
import com.example.useful_photo_album.databinding.FragmentSelectPhotoBinding
import com.example.useful_photo_album.viewmodels.ThemeListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectPhotoFragment : Fragment() {

    private val viewModel: ThemeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
            val binding = FragmentSelectPhotoBinding.inflate(inflater, container, false)
            context ?: return binding.root

            val adapter = ThemeAdapter()
            binding.themeList.adapter = adapter
            subscribeUi(adapter)

            return binding.root
    }

    private fun subscribeUi(adapter: ThemeAdapter) {
        viewModel.themes.observe(viewLifecycleOwner) { themes ->
            adapter.submitList(themes)
        }
    }

    private fun updateData() {
        with(viewModel) {
            if (isFiltered()) {
                clearFavoriteNumber()
            } else {
                setFavoriteNumber(2)
            }
        }
    }
}
