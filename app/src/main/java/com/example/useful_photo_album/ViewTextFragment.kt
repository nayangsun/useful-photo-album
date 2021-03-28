package com.example.useful_photo_album

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.useful_photo_album.ViewTextFragment.Callback
import com.example.useful_photo_album.data.model.PhotoWrite
import com.example.useful_photo_album.databinding.FragmentViewTextBinding
import com.example.useful_photo_album.viewmodels.ViewTextViewModel
import com.example.useful_photo_album.viewmodels.ViewTextViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs


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

            appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percent =
                    (abs(verticalOffset)).toFloat() / appBarLayout?.totalScrollRange!! // 0F to 1F

                detailImage.alpha = 1F - percent

            })
        }

        return binding.root
    }

    private fun statusText(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            view.systemUiVisibility = flags
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.apply {
                setDecorFitsSystemWindows(true)
            }
        }
    }

    fun interface Callback {
        fun delete(photo: PhotoWrite?)
    }
}