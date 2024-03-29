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

package com.example.useful_photo_album.presentation.agenda.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.useful_photo_album.presentation.agenda.ui.widget.AgendaHeadersDecoration
import com.example.useful_photo_album.presentation.agenda.ui.adapter.AgendaAdapter
import com.example.useful_photo_album.presentation.MainActivityViewModel
import com.example.useful_photo_album.presentation.MainNavigationFragment
import com.example.useful_photo_album.presentation.databinding.FragmentAgendaBinding
import com.example.useful_photo_album.shared.model.temp.Block
import com.example.useful_photo_album.shared.util.TimeUtils
import com.example.useful_photo_album.presentation.signin.setupProfileMenuItem
import com.example.useful_photo_album.presentation.util.clearDecorations
import com.example.useful_photo_album.presentation.util.doOnApplyWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.ZoneId

@AndroidEntryPoint
class AgendaFragment : MainNavigationFragment() {

    private val viewModel: AgendaViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentAgendaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgendaBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        // Pad the bottom of the RecyclerView so that the content scrolls up above the nav bar
        binding.recyclerView.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.toolbar.setupProfileMenuItem(mainActivityViewModel, viewLifecycleOwner)
    }
}

@BindingAdapter(value = ["agendaItems", "timeZoneId"])
fun agendaItems(recyclerView: RecyclerView, list: List<Block>?, zoneId: ZoneId?) {
    list ?: return
    zoneId ?: return
    val isInConferenceTimeZone = TimeUtils.isConferenceTimeZone(zoneId)
    if (recyclerView.adapter == null) {
        recyclerView.adapter = AgendaAdapter()
    }
    (recyclerView.adapter as AgendaAdapter).apply {
        submitList(list)
        timeZoneId = zoneId
    }
    // Recreate the decoration used for the sticky date headers
    recyclerView.clearDecorations()
    if (list.isNotEmpty()) {
        recyclerView.addItemDecoration(
            AgendaHeadersDecoration(recyclerView.context, list, isInConferenceTimeZone)
        )
    }
}
