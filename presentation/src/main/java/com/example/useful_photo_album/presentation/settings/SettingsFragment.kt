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

package com.example.useful_photo_album.presentation.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.databinding.FragmentSettingsBinding
import com.example.useful_photo_album.presentation.MainActivityViewModel
import com.example.useful_photo_album.presentation.MainNavigationFragment
import com.example.useful_photo_album.presentation.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : MainNavigationFragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        launchAndRepeatWithViewLifecycle {
            viewModel.navigationActions.collect {
                if (it is SettingsNavigationAction.NavigateToThemeSelector) {
                    ThemeSettingDialogFragment.newInstance()
                        .show(parentFragmentManager, null)
                }
            }
        }
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbar.setupProfileMenuItem(mainActivityViewModel, viewLifecycleOwner)

        binding.settingsScroll.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }

        return binding.root
    }
}

@BindingAdapter(value = ["dialogTitle", "fileLink"], requireAll = true)
fun createDialogForFile(button: View, dialogTitle: String, fileLink: String) {
    val context = button.context
    button.setOnClickListener {
        val webView = WebView(context).apply { loadUrl(fileLink) }
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setView(webView)
            .create()
            .show()
    }
}

@BindingAdapter("versionName")
fun setVersionName(view: TextView, versionName: String) {
    view.text = view.resources.getString(R.string.version_name, versionName)
}
