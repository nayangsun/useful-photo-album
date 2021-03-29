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

    private fun initAdapter() {
        searchAdapter = SearchAdapter()
    }

    private fun searchPhoto(theme: String) {
        initAdapter()

        binding.photoList.adapter = searchAdapter
        when (theme) {
            "random" -> {
//                randomAdapter = RandomPhotoAdapter()
//                binding.photoList.adapter = randomAdapter
                random()
            }
            else -> {
                search(theme)
            }
        }
    }

    private fun random() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
//            viewModel.randomPictures().collectLatest {
//                randomAdapter.submitList(it)
//            }
            viewModel.randomPictures()
                .catch {
                    /**
                     * [Comment-2.1]
                     * 이렇게 catch 를 해주면 api 호출시 예외발생해도 이벤트캐치가 가능합니다.
                     * coroutine flow 예외처리 관련해서 찾아보시면 자세하게 보실 수 있을거에요.
                     */
                    it.printStackTrace()
                }
                .collectLatest {
                Log.e("PhotoListFragment", "exception test log")
            }
            //[Comment-2] 이부분은 주석처리 풀면 정상작동 할겁니다.
//            viewModel.randomPicturesWithPaging()
//                .collectLatest {
//                searchAdapter.submitData(it)
//            }
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
//            viewModel.searchPictures(query).collectLatest {
//                searchAdapter.submitData(it)
//            }
            viewModel.searchPicturesWithPaging(query = query).collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }
}