package com.example.useful_photo_album.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.useful_photo_album.HomePhotoFragmentDirections
import com.example.useful_photo_album.data.model.PhotoWrite
import com.example.useful_photo_album.databinding.HomePhotoItemBinding


class HomePhotoAdapter : ListAdapter<PhotoWrite, RecyclerView.ViewHolder>(HomePhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomePhotoViewHolder(
            HomePhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = getItem(position)
        (holder as HomePhotoViewHolder).bind(photo)
    }

    class HomePhotoViewHolder(
        private val binding: HomePhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.photo?.let { photo ->
                    navigateToPhotoView(photo, it)
                }
            }
        }

        private fun navigateToPhotoView(
            photo: PhotoWrite,
            view: View
        ) {
            val direction =
                HomePhotoFragmentDirections.actionHomePhotoFragmentToViewTextFragment(photo.photoId)
            view.findNavController().navigate(direction)
        }


        fun bind(item: PhotoWrite) {
            binding.apply {
                photo = item
                executePendingBindings()
            }
        }
    }
}

private class HomePhotoDiffCallback : DiffUtil.ItemCallback<PhotoWrite>() {

    override fun areItemsTheSame(oldItem: PhotoWrite, newItem: PhotoWrite): Boolean {
        return oldItem.photoId == newItem.photoId
    }

    override fun areContentsTheSame(oldItem: PhotoWrite, newItem: PhotoWrite): Boolean {
        return oldItem == newItem
    }
}

