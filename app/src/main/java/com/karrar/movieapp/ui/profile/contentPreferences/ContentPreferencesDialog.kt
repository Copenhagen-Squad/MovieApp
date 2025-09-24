package com.karrar.movieapp.ui.profile.contentPreferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karrar.movieapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContentPreferencesDialog : BottomSheetDialogFragment() {

    private val viewModel: ContentPreferencesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = com.karrar.movieapp.databinding.ContentPreferencesDialogBinding.inflate(
            inflater, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.contentPreferencesUIEvent.collect { event ->
                event.getContentIfNotHandled()?.let { uiEvent ->
                    when (uiEvent) {
                        ContentPreferencesUIEvent.OnCloseDialog -> dismiss()
                    }
                }
            }
        }
    }
}


