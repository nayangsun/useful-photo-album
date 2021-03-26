package com.example.useful_photo_album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.useful_photo_album.data.model.PhotoWrite
import com.example.useful_photo_album.databinding.FragmentViewTextBinding
import com.example.useful_photo_album.viewmodels.ViewTextViewModel
import com.example.useful_photo_album.viewmodels.ViewTextViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewTextFragment : Fragment() {

    private val args: ViewTextFragmentArgs by navArgs()

    @Inject
    lateinit var viewTextViewModelFactory: ViewTextViewModelFactory

    private val viewTextViewModel: ViewTextViewModel by viewModels {
        ViewTextViewModel.provideFactory(viewTextViewModelFactory, args.photoId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentViewTextBinding>(
            inflater,
            R.layout.fragment_view_text,
            container,
            false
        ).apply {
            viewModel = viewTextViewModel
            lifecycleOwner = viewLifecycleOwner
            callback = Callback { photo ->
                photo?.let {
                    viewTextViewModel.deletePhotoWrite(photo)
                }
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    fun interface Callback {
        fun delete(photo: PhotoWrite?)
    }
}