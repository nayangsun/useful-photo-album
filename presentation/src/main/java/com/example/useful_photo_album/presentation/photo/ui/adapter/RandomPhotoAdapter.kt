package com.example.useful_photo_album.presentation.photo.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.useful_photo_album.PhotoListFragment
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.adapters.RandomPhotoAdapter.RandomPhotoViewHolder
import com.example.useful_photo_album.databinding.ListItemPhotoBinding

/**
 * Adapter for the [RecyclerView] in [PhotoListFragment].
 */
class RandomPhotoAdapter : ListAdapter<UnsplashPhoto, RandomPhotoViewHolder>(PhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomPhotoViewHolder {
        return RandomPhotoViewHolder(ListItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RandomPhotoViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            holder.bind(photo)
        }
    }

    class RandomPhotoViewHolder(
        private val binding: ListItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.photo?.let { photo ->
                    val uri = Uri.parse(photo.user.attributionUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    view.context.startActivity(intent)
                }
            }
        }

        fun bind(item: UnsplashPhoto) {
            binding.apply {
                photo = item

            }
        }
    }

}

private class PhotoDiffCallback : DiffUtil.ItemCallback<UnsplashPhoto>() {
    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem == newItem
    }
}
