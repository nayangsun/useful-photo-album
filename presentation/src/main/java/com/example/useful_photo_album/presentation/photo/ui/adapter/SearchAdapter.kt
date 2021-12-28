package com.example.useful_photo_album.presentation.photo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.useful_photo_album.PhotoListFragment
import com.example.useful_photo_album.PhotoListFragmentDirections
import com.example.useful_photo_album.adapters.SearchAdapter.SearchViewHolder
import com.example.useful_photo_album.data.model.PhotoWrite
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.databinding.ListItemPhotoBinding

/**
 * Adapter for the [RecyclerView] in [PhotoListFragment].
 */
class SearchAdapter : PagingDataAdapter<UnsplashPhoto, SearchViewHolder>(SearchDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ListItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            holder.bind(photo)
        }
    }

    class SearchViewHolder(
        private val binding: ListItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.photo?.let { unsplashPhoto ->
                    navigateToWrite(unsplashPhoto, it)
                }
            }
        }

        private fun navigateToWrite(
            photo: UnsplashPhoto,
            view: View
        ) {
            val direction =
                PhotoListFragmentDirections.actionPhotoListFragmentToWriteFragment(photo.urls.small, photo.id)
            view.findNavController().navigate(direction)
        }


        fun bind(item: UnsplashPhoto) {
            binding.apply {
                photo = item
                executePendingBindings()
            }
        }
    }
}

private class SearchDiffCallback : DiffUtil.ItemCallback<UnsplashPhoto>() {
    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem == newItem
    }
}