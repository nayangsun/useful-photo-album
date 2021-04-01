package com.example.useful_photo_album

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private lateinit var binding:FragmentPhotoListBinding

    private val searchAdapter = SearchAdapter()
    private val args: PhotoListFragmentArgs by navArgs()
    private var searchJob: Job? = null
    private val viewModel: PhotoListViewModel by viewModels()

    // pagingSource에서 예외처리를 처리해주기 때문에 별도로 설정할 필요가 없어졌지만, 기억하기 위해 남겨둔다.
    private lateinit var randomAdapter: RandomPhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.photoList.adapter = searchAdapter
        searchPhoto(args.theme)
    }

    private fun searchPhoto(theme: String) {
        when (theme) {
            "random" -> {
                random()
            }
            else -> {
                search(theme)
            }
        }
    }


    // 고민 - 한번 search한 값을 room에 저장한뒤 가져오도록 할까? (검색된 사진을 한번 보고 다시 돌아오면 재검색이 되어 버리니..)
    private fun random() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.randomPicturesWithPaging().collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPicturesWithPaging(query = query).collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }


    @Deprecated("replace to random()")
    private fun exRandom() {
        randomAdapter = RandomPhotoAdapter()
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.randomPictures()
                .catch {
                    it.printStackTrace()
                }
                .collectLatest {
                    Log.e("PhotoListFragment", "exception test log")
                }
        }
    }
}