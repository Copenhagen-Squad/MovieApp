package com.karrar.movieapp.ui.profile.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogChangeLanguageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageDialog : BottomSheetDialogFragment() {

    private var _binding: DialogChangeLanguageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LanguageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangeLanguageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.languageUIEvent.collect { event ->
                event.getContentIfNotHandled()?.let {
                    when (it) {
                        LanguageUIEvent.OnCloseDialog -> dismiss()
                    }
                }
            }
        }

        // English button
        view.findViewById<View>(R.id.english_chip).setOnClickListener {
            saveLanguage("en")
        }

        // Arabic button
        view.findViewById<View>(R.id.arabic_chip).setOnClickListener {
            saveLanguage("ar")
        }

        // Close button
        view.findViewById<View>(R.id.close_dialog).setOnClickListener {
            dismiss()
        }
    }

    private fun saveLanguage(lang: String) {
        val prefs = requireContext().getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)
        prefs.edit().putString("app_lang", lang).apply()

        dismiss()
        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
