package com.karrar.movieapp.ui.profile.myratings


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMyRatingsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyRatingsFragment : BaseFragment<FragmentMyRatingsBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_my_ratings
    override val viewModel: MyRatingsViewModel by viewModels()

    private lateinit var adapter: RatedMoviesAdapter
    private var displayedList: List<RatedUIState> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)

        setupAdapter()
        observeViewModel()
        setupTabLayout()
        collectEvents()
    }

    private fun setupAdapter() {
        adapter = RatedMoviesAdapter(emptyList(), viewModel)
        binding.recyclerViewRatedMovies.adapter = adapter
    }

    private fun setupTabLayout() {
        binding.tabLayout.layout.addTab(binding.tabLayout.layout.newTab().setText("Movies"))
        binding.tabLayout.layout.addTab(binding.tabLayout.layout.newTab().setText("Series"))
        binding.tabLayout.layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.selectTab(Constants.MOVIE)
                    1 -> viewModel.selectTab(Constants.TV_SHOWS)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun collectEvents() {
        collectLast(viewModel.myRatingUIEvent) { event ->
            event.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: MyRatingUIEvent) {
        when (event) {
            is MyRatingUIEvent.MovieEvent -> {
                MyRatingsFragmentDirections.actionRatedMoviesFragmentToMovieDetailFragment(event.movieID)
            }

            is MyRatingUIEvent.TVShowEvent -> {
                MyRatingsFragmentDirections.actionRatedMoviesFragmentToTvShowDetailsFragment(event.tvShowID)
            }

            MyRatingUIEvent.BackEvent -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.ratedUiState.collectLatest { state ->
                displayedList = state.ratedList
                adapter = RatedMoviesAdapter(displayedList, viewModel)
                binding.recyclerViewRatedMovies.adapter = adapter
            }
        }
    }
}