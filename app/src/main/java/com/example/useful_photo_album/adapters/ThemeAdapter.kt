package com.example.useful_photo_album.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.useful_photo_album.SelectPhotoFragment
import com.example.useful_photo_album.SelectPhotoFragmentDirections
import com.example.useful_photo_album.data.model.PhotoTheme
import com.example.useful_photo_album.databinding.ListItemThemeBinding

/**
 * Adapter for the [RecyclerView] in [SelectPhotoFragment].
 */
class ThemeAdapter : ListAdapter<PhotoTheme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThemeViewHolder(
            ListItemThemeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        (holder as ThemeViewHolder).bind(theme)
    }

    class ThemeViewHolder(
        private val binding: ListItemThemeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.theme?.let { theme ->
                    navigateToPhotoList(theme, it)
                }
            }
        }

        private fun navigateToPhotoList(
            theme: PhotoTheme,
            view: View
        ) {
            val direction =
                SelectPhotoFragmentDirections.actionSelectPhotoFragmentToPhotoListFragment(theme.theme)
            view.findNavController().navigate(direction)
        }

        fun bind(item: PhotoTheme) {
            binding.apply {
                theme = item
                executePendingBindings()
            }
        }
    }
}

private class ThemeDiffCallback : DiffUtil.ItemCallback<PhotoTheme>() {

    override fun areItemsTheSame(oldItem: PhotoTheme, newItem: PhotoTheme): Boolean {
        return oldItem.theme == newItem.theme
    }

    override fun areContentsTheSame(oldItem: PhotoTheme, newItem: PhotoTheme): Boolean {
        return oldItem == newItem
    }
}
