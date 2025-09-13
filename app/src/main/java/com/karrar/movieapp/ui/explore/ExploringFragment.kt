package com.karrar.movieapp.ui.explore

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentExploringBinding
import com.karrar.movieapp.ui.adapters.LoadUIStateAdapter
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.explore.exploreUIState.ExploreDisplayMode
import com.karrar.movieapp.ui.explore.exploreUIState.ExploringUIEvent
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIState
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collect
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setSpanSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploringFragment : BaseFragment<FragmentExploringBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_exploring
    override val viewModel: ExploringViewModel by viewModels()

    private val gridAdapter by lazy { ExploreGridAdapter(viewModel) }
    private val listAdapter by lazy { ExploreListAdapter(viewModel) }
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setTitle(false)
        setupToggleButtons()
        setupGenreAdapter()
        setupMediaAdapter()
        observeData()
        observeEvents()
    }

    private fun setupToggleButtons() {
        binding.viewGridButton.setOnClickListener {
            viewModel.onClickViewMode(ExploreDisplayMode.GRID)
        }
        binding.viewListButton.setOnClickListener {
            viewModel.onClickViewMode(ExploreDisplayMode.LIST)
        }
    }

    private fun setupGenreAdapter() {
        genreAdapter = GenreAdapter(emptyList(), viewModel)
        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    private fun setupMediaAdapter(viewMode: ExploreDisplayMode = ExploreDisplayMode.GRID) {
        val currentAdapter = if (viewMode == ExploreDisplayMode.GRID) gridAdapter else listAdapter
        val footerAdapter = LoadUIStateAdapter(currentAdapter::retry)

        binding.recyclerMedia.apply {
            adapter = currentAdapter.withLoadStateFooter(footerAdapter)
            layoutManager = createLayoutManager(viewMode, footerAdapter, currentAdapter)
        }

        collect(currentAdapter.loadStateFlow) { viewModel.setErrorUiState(it) }

        lifecycleScope.launch {
            currentAdapter.submitData(viewModel.uiState.value.media.first())
        }
    }

    private fun createLayoutManager(
        viewMode: ExploreDisplayMode,
        footerAdapter: LoadUIStateAdapter,
        currentAdapter: Any
    ) = when (viewMode) {
        ExploreDisplayMode.GRID -> {
            GridLayoutManager(requireContext(), 2).apply {
                setSpanSize(footerAdapter, currentAdapter as ExploreGridAdapter, spanCount)
            }
        }
        ExploreDisplayMode.LIST -> {
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                genreAdapter.setItems(state.genres)
                genreAdapter.setSelectedGenre(state.selectedCategoryID)

                collectLast(state.media) { data ->
                    when (state.selectedViewMode) {
                        ExploreDisplayMode.GRID -> gridAdapter.submitData(data)
                        ExploreDisplayMode.LIST -> listAdapter.submitData(data)
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        collectLast(viewModel.exploringUIEvent) { event ->
            event?.getContentIfNotHandled()?.let(::handleEvent)
        }
    }

    private fun handleEvent(event: ExploringUIEvent) {
        when (event) {
            ExploringUIEvent.RetryEvent -> gridAdapter.retry()
            ExploringUIEvent.SearchEvent -> navigateToSearch()
            is ExploringUIEvent.SelectedCategory ->
                viewModel.getMediaList(selectedCategory = event.categoryID)
            is ExploringUIEvent.SelectedMediaType ->
                viewModel.getMediaList(selectedMediaType = event.mediaTypeID)
            is ExploringUIEvent.ClickMediaEvent ->
                navigateToMediaDetails(event.mediaItem)
            is ExploringUIEvent.SelectedViewMode ->
                setupMediaAdapter(event.viewMode)
        }
    }

    private fun navigateToSearch() {
        val extras = FragmentNavigatorExtras(binding.inputSearch to "search_box")
        findNavController().navigate(
            ExploringFragmentDirections.actionExploringFragmentToSearchFragment(),
            extras
        )
    }

    private fun navigateToMediaDetails(item: MediaUIState) {
        val direction = when (item.mediaType) {
            Constants.MOVIE -> ExploringFragmentDirections
                .actionExploringFragmentToMovieDetailFragment(item.mediaID)
            Constants.TV_SHOWS -> ExploringFragmentDirections
                .actionExploringFragmentToTvShowDetailsFragment(item.mediaID)
            else -> return
        }
        findNavController().navigate(direction)
    }
}