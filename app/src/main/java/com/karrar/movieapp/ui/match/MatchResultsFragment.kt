package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchResultBinding
import com.karrar.movieapp.ui.base.BaseFragment
import kotlinx.coroutines.launch

class MatchResultsFragment : BaseFragment<FragmentMatchResultBinding>() {

    override val layoutIdFragment: Int = R.layout.fragment_match_result
    override val viewModel: MatchResultsViewModel by viewModels()

    private val posterAdapter = MatchPostersAdapter()
    private var items: List<MatchItemUI> = emptyList()

    private val pageCb = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (items.isNotEmpty() && position < items.size) {
                binding.item = items[position]
                binding.executePendingBindings()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, getString(R.string.your_match_results))

        setupCallbacks()
        setupViewPager()
        collectData()

        // Load match results
        viewModel.loadMatchResults()
    }

    private fun setupCallbacks() {
        binding.callbacks = object : MatchResultCallbacks {
            override fun onViewDetails(item: MatchItemUI) {
                // Navigate to movie/TV show details
                if (item.isMovie) {
                    findNavController().navigate(
                        R.id.action_matchResultsFragment_to_movieDetailFragment,
                        Bundle().apply { putInt("movie_id", item.id) }
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_matchResultsFragment_to_tvShowDetailsFragment,
                        Bundle().apply { putInt("tvShowId", item.id) }
                    )
                }
            }

            override fun onPlay(item: MatchItemUI) {
                // Navigate to YouTube player
                findNavController().navigate(
                    R.id.action_matchResultsFragment_to_youtubePlayerActivity,
                    Bundle().apply {
                        putInt("movie_id", item.id)
                        putSerializable("type", if (item.isMovie) com.karrar.movieapp.domain.enums.MediaType.MOVIE else com.karrar.movieapp.domain.enums.MediaType.TV_SHOW)
                    }
                )
            }

            override fun onSave(item: MatchItemUI) {
                // Show save dialog
                findNavController().navigate(
                    R.id.action_matchResultsFragment_to_saveMovieDialog,
                    Bundle().apply { putInt("movie_id", item.id) }
                )
            }

            override fun onDetails(item: MatchItemUI) {
                onViewDetails(item)
            }
        }
    }

    private fun setupViewPager() {
        binding.poster.adapter = posterAdapter
        binding.poster.registerOnPageChangeCallback(pageCb)
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.items.collect { newItems ->
                updateItems(newItems)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.selectedItem != null) {
                    // Handle any selected item actions if needed
                }
            }
        }
    }

    private fun updateItems(newItems: List<MatchItemUI>) {
        items = newItems
        if (items.isEmpty()) return

        binding.item = items.first()
        posterAdapter.submitList(items) {
            binding.poster.post {
                setupViewPagerStyling()
            }
        }
    }

    private fun setupViewPagerStyling() {
        val pager = binding.poster
        val rv = pager.getChildAt(0) as RecyclerView

        val PAGE_WIDTH_FRACTION = 0.74f
        val pageWidth = (pager.width * PAGE_WIDTH_FRACTION).toInt()
        val side = (pager.width - pageWidth) / 2

        rv.setPadding(side, 0, side, 0)
        rv.clipToPadding = false
        rv.overScrollMode = View.OVER_SCROLL_NEVER
        pager.offscreenPageLimit = 3

        pager.setPageTransformer { page, position ->
            val p = kotlin.math.abs(position)
            val scale = 0.90f + (1f - p) * 0.10f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.80f + (1f - p) * 0.20f
        }

        val middle = items.size / 2
        pager.setCurrentItem(middle, false)

        binding.item = items[middle]
        binding.executePendingBindings()
    }
}