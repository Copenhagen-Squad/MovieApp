package com.karrar.movieapp.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentProfileBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.components.CustomSwitch
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        collectLast(viewModel.profileUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }

        val versionName = BuildConfig.VERSION_NAME
        binding.appVersion.text = "Version $versionName"
        binding.appVersionLogout.text = "Version $versionName"

        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val themeMode = prefs.getString("theme_mode", null)
        val isDark = when (themeMode) {
            "dark" -> true
            "light" -> false
            else -> {
                resources.configuration.uiMode and
                        android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                        android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
        }

        setupDarkModeSwitch(binding.darkModeSwitch, prefs, isDark, enabled = true)
        setupDarkModeSwitch(binding.darkModeSwitchPlaceholder, prefs, isDark, enabled = false)

        collectLast(viewModel.profileUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun setupDarkModeSwitch(
        switch: CustomSwitch,
        prefs: SharedPreferences,
        isDark: Boolean,
        enabled: Boolean
    ) {
        switch.setChecked(isDark)
        switch.isEnabled = enabled
        switch.setOnCheckedChangeListener { isOn ->
            if (isOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs.edit().putString("theme_mode", "dark").apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs.edit().putString("theme_mode", "light").apply()
            }
        }
    }

    private fun onEvent(event: ProfileUIEvent) {
        val action = when (event) {
            ProfileUIEvent.DialogLogoutEvent -> {
                ProfileFragmentDirections.actionProfileFragmentToLogoutDialog()
            }
            ProfileUIEvent.LoginEvent -> {
                ProfileFragmentDirections.actionProfileFragmentToLoginFragment(Constants.PROFILE)
            }
            ProfileUIEvent.RatedMoviesEvent -> {
                ProfileFragmentDirections.actionProfileFragmentToRatedMoviesFragment()
            }
            ProfileUIEvent.WatchHistoryEvent -> {
                ProfileFragmentDirections.actionProfileFragmentToWatchHistoryFragment()
            }
            ProfileUIEvent.MyCollectionEvent -> {
                ProfileFragmentDirections.actionProfileFragmentToMyCollectionFragment()
            }
            ProfileUIEvent.OnClickChangeLanguage -> {
                ProfileFragmentDirections.actionProfileFragmentToLanguageDialog()
            }
            ProfileUIEvent.OnClickContentPreferences -> {
                ProfileFragmentDirections.actionProfileFragmentToContentPreferencesDialog()
            }
            ProfileUIEvent.OnClickEditProfile -> {
                ProfileFragmentDirections.actionProfileFragmentToEditProfileDialog()
            }
        }
        findNavController().navigate(action)
    }

}