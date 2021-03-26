package com.example.useful_photo_album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.useful_photo_album.adapters.RandomPhotoAdapter
import com.example.useful_photo_album.adapters.SearchAdapter
import com.example.useful_photo_album.databinding.FragmentPhotoListBinding
import com.example.useful_photo_album.viewmodels.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private lateinit var binding: FragmentPhotoListBinding
    private lateinit var randomAdapter: RandomPhotoAdapter
    private lateinit var searchAdapter: SearchAdapter

    private val args: PhotoListFragmentArgs by navArgs()
    private var searchJob: Job? = null
    private val viewModel: PhotoListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        searchPhoto(args.theme)

        return binding.root
    }

    private fun searchPhoto(theme: String) {
        when (theme) {
            "random" -> {
                randomAdapter = RandomPhotoAdapter()
                binding.photoList.adapter = randomAdapter
                random()
            }
            else -> {
                searchAdapter = SearchAdapter()
                binding.photoList.adapter = searchAdapter
                search(theme)
            }
        }
    }

    private fun random() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.randomPictures().collectLatest {
                randomAdapter.submitList(it)
            }
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPictures(query).collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }
}