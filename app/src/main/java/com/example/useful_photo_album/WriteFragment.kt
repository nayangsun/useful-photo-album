package com.example.useful_photo_album

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.useful_photo_album.databinding.FragmentWriteBinding
import com.example.useful_photo_album.viewmodels.WriteViewModel
import com.example.useful_photo_album.viewmodels.WriteViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WriteFragment : Fragment() {

    private lateinit var binding: FragmentWriteBinding
    private val args: WriteFragmentArgs by navArgs()

    @Inject
    lateinit var writeViewModelFactory: WriteViewModelFactory

    private val writeViewModel: WriteViewModel by viewModels {
        WriteViewModel.provideFactory(writeViewModelFactory, args.photoId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentWriteBinding>(
                inflater,
                R.layout.fragment_write,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            url = args.imageUrl
            toolbar.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.submit -> {
                        doSubmit()
                        true
                    }
                    else -> false
                }
            }
        }
    }


    private fun doSubmit() {
        with(binding) {
            val title = title.text.toString()
            val description = description.text.toString()
            writeViewModel.addWritePhoto(title, description, url)
        }
        navigateToHomePhoto()

    }

    private fun navigateToHomePhoto() {
        val direction =
            WriteFragmentDirections.actionWriteFragmentToHomePhotoFragment()
        findNavController().navigate(direction)
    }
}