package com.example.useful_photo_album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
class HomePhotoFragment : Fragment() {

    private lateinit var binding: FragmentHomePhotoBinding
    private val viewModel: HomePhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePhotoBinding.inflate(inflater, container, false)
        /**
         * binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_photo, container, false)
         * 이부분은 이미 binding 변수의 타입을 미리 알고 있냐? 차이인 것 같습니다.
         * (저는 그렇게 알고 사용하고 있어요..ㅎㅎ 이론적인 부분은 사실...패스...)
         * 변수의 타입이 FragmentHomePhotoBinding 라는걸 이미 알고 있기때문에
         * FragmentHomePhotoBinding 로 inflate 할때는 별도의 layout resource 가 포함이 안되구요,
         * 반대로 DataBindingUtil 의 경우엔 타입을 모르기때문에 layout resource Id 가 추가로 포함되는 것입니다.
         *
         * DataBindingUtil 같은경우엔 뭐 예를들며 BaseFragment 에서 공용코드로 사용할때 Generic 타입을 이요해서 많이 사용합니다.
         * class BaseFragment<BINDING: ViewDataBinding> {
         *     lateinit var binding: BINDING
         *     ...
         *     binding = DataBindingUtil.inflate(inflater, {layoutId}, container, false)
         * }
         *
         * 어느것을 많이 사용한다는 뭐 상황에 따라 다르거나 개발자 취향 아닐까 싶네요. 저는 그렇게 생각합니다 ㅋㅋ
         */
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
        return binding.root
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

