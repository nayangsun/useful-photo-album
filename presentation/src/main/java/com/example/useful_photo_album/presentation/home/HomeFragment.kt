package com.example.useful_photo_album.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.useful_photo_album.adapters.HomePhotoAdapter
import com.example.useful_photo_album.databinding.FragmentHomePhotoBinding
import com.example.useful_photo_album.viewmodels.HomePhotoViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomePhotoBinding
    private val viewModel: HomePhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = HomePhotoAdapter()
        binding.apply {
            writeList.adapter = adapter
            writeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            url = "https://upload.wikimedia.org/wikipedia/commons/6/6e/Reflections_in_a_row_%28explored_2016-10-03%29_-_Flickr_-_Maria_Eklind.jpg"

            addPhoto.setOnClickListener {
                navigateToSelectPhotoPage(it)
            }

            toolbar.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_share -> {
                        view?.let {
                            navigateToSelectPhotoPage(it)
                        }
                        true
                    }
                    R.id.full_screen -> {
                        view?.let {
                            navigateToSelectFullScreen(it)
                        }
                        true
                    }
                    else -> false
                }
            }

            appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percent =
                    (abs(verticalOffset)).toFloat() / appBarLayout?.totalScrollRange!! // 0F to 1F
                appbarContainer.alpha = 1F - percent
            })
        }

        subscribeUi(adapter, binding)
    }

    private fun subscribeUi(adapter: HomePhotoAdapter, binding: FragmentHomePhotoBinding) {
        viewModel.photoWritings.observe(viewLifecycleOwner) { result ->
            binding.hasWritings = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }

    private fun navigateToSelectPhotoPage(view: View) {
        val direction =
            HomePhotoFragmentDirections.actionHomePhotoFragmentToSelectPhotoFragment()
        view.findNavController().navigate(direction)
    }

    private fun navigateToSelectFullScreen(view: View) {
        val direction =
            HomePhotoFragmentDirections.actionHomePhotoFragmentToFullscreenFragment()
        view.findNavController().navigate(direction)
    }
}

