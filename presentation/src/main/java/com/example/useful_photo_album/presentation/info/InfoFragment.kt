/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.useful_photo_album.presentation.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.databinding.FragmentInfoBinding
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.google.android.material.tabs.TabLayoutMediator
import com.example.useful_photo_album.presentation.MainActivityViewModel
import com.example.useful_photo_album.presentation.MainNavigationFragment
import com.example.useful_photo_album.presentation.signin.setupProfileMenuItem
import com.example.useful_photo_album.presentation.util.doOnApplyWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InfoFragment : MainNavigationFragment() {

    @Inject lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var binding: FragmentInfoBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.viewpager.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            toolbar.setupProfileMenuItem(viewModel, viewLifecycleOwner)

            viewpager.offscreenPageLimit = INFO_PAGES.size
            viewpager.adapter = InfoAdapter(this@InfoFragment)

            TabLayoutMediator(tabs, viewpager) { tab, position ->
                tab.text = resources.getString(INFO_TITLES[position])
            }.attach()

            // Analytics. Manually fire once for the loaded tab, then fire on tab change.
            trackInfoScreenView(0)
            viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    trackInfoScreenView(position)
                }
            })
        }
    }

    private fun trackInfoScreenView(position: Int) {
        val pageName = getString(INFO_TITLES[position])
        analyticsHelper.sendScreenView("Info - $pageName", requireActivity())
    }

    /**
     * Adapter that builds a page for each info screen.
     */
    inner class InfoAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = INFO_PAGES[position].invoke()

        override fun getItemCount() = INFO_PAGES.size
    }

    companion object {

        private val INFO_TITLES = arrayOf(
            R.string.event_title,
            R.string.travel_title,
            R.string.faq_title
        )
        private val INFO_PAGES = arrayOf(
            { EventFragment() },
            { TravelFragment() },
            { FaqFragment() }
            // TODO: Track the InfoPage performance b/130335745
        )
    }
}
