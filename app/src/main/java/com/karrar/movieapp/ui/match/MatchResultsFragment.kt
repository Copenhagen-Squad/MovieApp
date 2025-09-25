package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchResultBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.MovieDetailsUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchResultsFragment : BaseFragment<FragmentMatchResultBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_match_result
    override val viewModel: MatchResultsViewModel by viewModels()
    private val posterAdapter = MatchPostersAdapter()
    private var items: List<MovieDetailsUIState> = emptyList()
    private var isViewPagerStyled = false

    private val pageCb = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (items.isNotEmpty() && position >= 0 && position < items.size) {
                binding.item = items[position]
                binding.executePendingBindings()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)

        setupCallbacks()
        setupViewPager()
        collectData()
    }

    private fun setupCallbacks() {
        binding.callbacks = object : MatchResultCallbacks {
            override fun onViewDetails(item: MovieDetailsUIState) {
                findNavController().navigate(
                    R.id.action_matchResultsFragment_to_movieDetailFragment,
                    Bundle().apply { putInt("movie_id", item.id) }
                )
            }

            override fun onPlay(item: MovieDetailsUIState) {
                findNavController().navigate(
                    R.id.action_matchResultsFragment_to_youtubePlayerActivity,
                    Bundle().apply {
                        putInt("movie_id", item.id)
                        putSerializable("type", com.karrar.movieapp.domain.enums.MediaType.MOVIE)
                    }
                )
            }

            override fun onSave(item: MovieDetailsUIState) {
                findNavController().navigate(
                    R.id.action_matchResultsFragment_to_saveMovieDialog,
                    Bundle().apply { putInt("movie_id", item.id) }
                )
            }

            override fun onDetails(item: MovieDetailsUIState) {
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
                if (state.selectedItem != null && items.isNotEmpty()) {
                    val index = items.indexOfFirst { it.id == state.selectedItem.id }
                    if (index >= 0 && index < items.size) {
                        binding.poster.setCurrentItem(index, true)
                    }
                }
            }
        }
    }

    private fun updateItems(newItems: List<MovieDetailsUIState>) {
        items = newItems
        if (items.isEmpty()) return

        binding.item = items.first()
        posterAdapter.submitList(items) {
            isViewPagerStyled = false

            binding.poster.post {
                setupViewPagerStylingWithDelay()
            }
        }
    }

    private fun setupViewPagerStylingWithDelay() {
        val pager = binding.poster

        if (isViewPagerStyled ||
            items.isEmpty() ||
            pager.adapter == null ||
            pager.adapter?.itemCount == 0
        ) {
            return
        }
        pager.viewTreeObserver.addOnGlobalLayoutListener(object :
            android.view.ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                pager.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (!isViewPagerStyled &&
                    pager.width > 0 &&
                    pager.height > 0 &&
                    (pager.adapter?.itemCount ?: 0) > 0
                ) {

                    setupViewPagerStyling()
                }
            }
        })
    }

    private fun setupViewPagerStyling() {
        val pager = binding.poster

        try {
            isViewPagerStyled = true
            if (pager.width <= 0 || pager.height <= 0 || items.isEmpty()) {
                isViewPagerStyled = false
                return
            }
            pager.layoutDirection = View.LAYOUT_DIRECTION_LTR
            val rv = if (pager.childCount > 0) {
                pager.getChildAt(0) as? RecyclerView
            } else {
                null
            }

            if (rv == null) {
                isViewPagerStyled = false
                return
            }
            rv.layoutDirection = View.LAYOUT_DIRECTION_LTR
            val PAGE_WIDTH_FRACTION = 0.74f
            val pageWidth = (pager.width * PAGE_WIDTH_FRACTION).toInt()
            val side = (pager.width - pageWidth) / 2
            if (pageWidth <= 0 || side < 0) {
                isViewPagerStyled = false
                return
            }
            rv.setPadding(side, 0, side, 0)
            rv.clipToPadding = false
            rv.overScrollMode = View.OVER_SCROLL_NEVER

            pager.offscreenPageLimit = 3
            pager.post {
                try {
                    if (pager.width > 0) {
                        pager.setPageTransformer { page, position ->
                            val p = kotlin.math.abs(position)
                            val scale = 0.90f + (1f - p) * 0.10f
                            page.scaleX = scale
                            page.scaleY = scale
                            page.alpha = 0.80f + (1f - p) * 0.20f
                        }

                        val middle = items.size / 2
                        pager.post {
                            try {
                                pager.setCurrentItem(middle, false)
                                if (middle < items.size) {
                                    binding.item = items[middle]
                                    binding.executePendingBindings()
                                }
                            } catch (e: Exception) {
                                pager.setCurrentItem(0, false)
                                binding.item = items[0]
                                binding.executePendingBindings()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isViewPagerStyled = false
                }
            }

        } catch (e: Exception) {
            isViewPagerStyled = false
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.poster.unregisterOnPageChangeCallback(pageCb)
        isViewPagerStyled = false
    }
}