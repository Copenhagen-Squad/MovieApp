package com.karrar.movieapp.ui.main

import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHome: LinearLayout
    private lateinit var navExplore: LinearLayout
    private lateinit var navMatch: LinearLayout
    private lateinit var navMe: LinearLayout

    private lateinit var iconHome: ImageView
    private lateinit var iconExplore: ImageView
    private lateinit var iconMatch: ImageView
    private lateinit var iconMe: ImageView

    private lateinit var labelHome: TextView
    private lateinit var labelExplore: TextView
    private lateinit var labelMatch: TextView
    private lateinit var labelMe: TextView

    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        setTheme(R.style.Theme_MovieApp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        installSplashScreen()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeCustomNavigation()
    }

    override fun onResume() {
        super.onResume()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.exploringFragment,
                R.id.myListFragment,
                R.id.profileFragment,
            )
        )
        navController = findNavController(R.id.nav_host_fragment)

        setupCustomBottomNavigation()
    }

    private fun initializeCustomNavigation() {
        // Initialize custom bottom navigation views
        navHome = binding.navHome
        navExplore = binding.navExplore
        navMatch = binding.navMatch
        navMe = binding.navMe

        iconHome = binding.iconHome
        iconExplore = binding.iconExplore
        iconMatch = binding.iconMatch
        iconMe = binding.iconMe

        labelHome = binding.labelHome
        labelExplore = binding.labelExplore
        labelMatch = binding.labelMatch
        labelMe = binding.labelMe
    }

    private fun setupCustomBottomNavigation() {
        navHome.setOnClickListener {
            navigateToDestination(R.id.homeFragment, 0)
        }
        navExplore.setOnClickListener {
            navigateToDestination(R.id.exploringFragment, 1)
        }
        navMatch.setOnClickListener {
            navigateToDestination(R.id.myListFragment, 2)
        }
        navMe.setOnClickListener {
            navigateToDestination(R.id.profileFragment, 3)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateTabSelection(destination.id)
        }

        updateTabSelection(R.id.homeFragment)
    }

    private fun navigateToDestination(destinationId: Int, tabIndex: Int) {
        if (currentTab == tabIndex) return

        currentTab = tabIndex

        try {
            navController.navigate(destinationId)
        } catch (e: IllegalArgumentException) {
            // Handle case where destination might not be accessible from current location
            navController.popBackStack(destinationId, false)
                ?: navController.navigate(destinationId)
        }
    }

    private fun updateTabSelection(destinationId: Int) {
        resetAllTabs()

        when (destinationId) {
            R.id.homeFragment -> {
                currentTab = 0
                setTabSelected(iconHome, labelHome)
            }
            R.id.exploringFragment -> {
                currentTab = 1
                setTabSelected(iconExplore, labelExplore)
            }
            R.id.myListFragment -> {
                currentTab = 2
                setTabSelected(iconMatch, labelMatch)
            }
            R.id.profileFragment -> {
                currentTab = 3
                setTabSelected(iconMe, labelMe)
            }
        }
    }

    private fun resetAllTabs() {
        setTabUnselected(iconHome, labelHome)
        setTabUnselected(iconExplore, labelExplore)
        setTabUnselected(iconMatch, labelMatch)
        setTabUnselected(iconMe, labelMe)
    }

    private fun setTabSelected(icon: ImageView, label: TextView) {
        icon.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
        label.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
    }

    private fun setTabUnselected(icon: ImageView, label: TextView) {
        icon.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
        label.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}