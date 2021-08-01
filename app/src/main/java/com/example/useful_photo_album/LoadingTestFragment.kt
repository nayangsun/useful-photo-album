package com.example.useful_photo_album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import com.example.useful_photo_album.adapters.RandomPhotoAdapter
import com.example.useful_photo_album.adapters.SearchAdapter
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.databinding.FragmentLoadingTestBinding
import com.example.useful_photo_album.viewmodels.LoadingTestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoadingTestFragment : Fragment() {

    private lateinit var binding: FragmentLoadingTestBinding

    @Inject
    lateinit var appExecutors: AppExecutors

    private val searchAdapter = SearchAdapter()
    private val randomAdapter = RandomPhotoAdapter()
    private var searchJob: Job? = null
    private val args: LoadingTestFragmentArgs by navArgs()
    private val viewModel: LoadingTestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingTestBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.photoList.adapter = searchAdapter
        binding.photoList.adapter = randomAdapter
        binding.searchResult = viewModel.results

        viewModel.results.observe(viewLifecycleOwner) { result ->
            randomAdapter.submitList(result?.data)
        }

//        viewModel.setQuery(args.theme)
//        viewModel.searchResults.observe(viewLifecycleOwner) { search ->
//            search?.data?.let {
//                loadData(it)
//            }
//        }
    }

    
//    private fun loadData(data: PagingData<UnsplashPhoto>) {
//        // Make sure we cancel the previous job before creating a new one
//        searchJob?.cancel()
//        searchJob = lifecycleScope.launch {
//            binding.lifecycleOwner?.let {
//                searchAdapter.submitData(it.lifecycle, data)
//            }
//        }
//    }

}