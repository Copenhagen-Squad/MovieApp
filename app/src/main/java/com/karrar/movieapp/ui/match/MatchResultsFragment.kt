package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchResultBinding
import com.karrar.movieapp.ui.movieDetails.DetailInteractionListener
import com.karrar.movieapp.ui.movieDetails.MovieDetailsUIEvent
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.MovieDetailsUIState
import com.karrar.movieapp.utilities.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchResultsFragment : Fragment(R.layout.fragment_match_result), DetailInteractionListener {

    private var _binding: FragmentMatchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var posterAdapter: MatchPostersAdapter
    private var items: List<MatchItemUI> = emptyList()

    // Event handling for movie details interactions
    private val _movieDetailsUIEvent = MutableStateFlow<Event<MovieDetailsUIEvent?>>(Event(null))
    val movieDetailsUIEvent: StateFlow<Event<MovieDetailsUIEvent?>> = _movieDetailsUIEvent.asStateFlow()

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position in items.indices) {
                updateCurrentItemBinding(position)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchResultBinding.bind(view)

        setupUI()
        observeEvents()
        loadDemoData()
    }

    private fun setupUI() {
        posterAdapter = MatchPostersAdapter()

        // Set the listener for data binding interactions
        binding.listener = this

        // Setup callbacks for match result interactions
        binding.callbacks = object : MatchResultCallbacks {
            override fun onViewDetails(item: MatchItemUI) {
                navigateToMovieDetails(item.id)
            }

            override fun onPlay(item: MatchItemUI) {
                playTrailer(item)
            }

            override fun onSave(item: MatchItemUI) {
                saveMovie(item)
            }

            override fun onDetails(item: MatchItemUI) {
                navigateToMovieDetails(item.id)
            }
        }

        // Setup ViewPager2
        binding.poster.apply {
            adapter = posterAdapter
            registerOnPageChangeCallback(pageChangeCallback)
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieDetailsUIEvent.collect { event ->
                event.getContentIfNotHandled()?.let { uiEvent ->
                    handleUIEvent(uiEvent)
                }
            }
        }
    }

    private fun handleUIEvent(uiEvent: MovieDetailsUIEvent) {
        when (uiEvent) {
            is MovieDetailsUIEvent.ClickPlayTrailerEvent -> {
                getCurrentItem()?.let { item ->
                    showTrailerFeedback(item)
                }
            }
            is MovieDetailsUIEvent.ClickSaveEvent -> {
                getCurrentItem()?.let { item ->
                    showSaveFeedback(item)
                }
            }
            is MovieDetailsUIEvent.ClickBackEvent -> {
                navigateBack()
            }
            is MovieDetailsUIEvent.ClickReviewsEvent -> {
                getCurrentItem()?.let { item ->
                    showReviewsFeedback(item)
                }
            }
            is MovieDetailsUIEvent.ClickMovieEvent -> {
                navigateToMovieDetails(uiEvent.movieID)
            }
            is MovieDetailsUIEvent.ClickCastEvent -> {
                navigateToCastDetails(uiEvent.castID)
            }
            is MovieDetailsUIEvent.MessageAppear -> {
                // Handle message appearance if needed
            }
        }
    }

    private fun loadDemoData() {
        val demoItems = createDemoData()
        updateItems(demoItems)
    }

    private fun createDemoData(): List<MatchItemUI> {
        return listOf(
            MatchItemUI(
                id = 1,
                title = "Until Dawn",
                posterUrl = "https://image.tmdb.org/t/p/w500/8a1p4pQK8C9ZK1rK8Zk.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/8a1p4pQK8C9ZK1rK8Zk.jpg",
                genres = "Horror, Mystery, Science Fiction",
                voteAverage = 8.5,
                runtimeFormatted = "2h 32m",
                releaseDateShort = "2008, Jul 18",
                isMovie = true
            ),
            MatchItemUI(
                id = 2,
                title = "Fountain of Youth",
                posterUrl = "https://image.tmdb.org/t/p/w500/aaaBBBcccDDD.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/aaaBBBcccDDD.jpg",
                genres = "Adventure, Fantasy, Mystery",
                voteAverage = 7.9,
                runtimeFormatted = "1h 58m",
                releaseDateShort = "2016, May 05",
                isMovie = true
            ),
            MatchItemUI(
                id = 3,
                title = "The Dark Knight",
                posterUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                genres = "Drama, Action, Crime, Thriller",
                voteAverage = 9.0,
                runtimeFormatted = "2h 32m",
                releaseDateShort = "2008, Jul 18",
                isMovie = false
            )
        )
    }

    private fun updateItems(newItems: List<MatchItemUI>) {
        if (newItems.isEmpty()) {
            showEmptyState()
            return
        }

        items = newItems

        posterAdapter.submitList(items) {
            setupViewPagerAppearance()
        }
    }

    private fun updateCurrentItemBinding(position: Int) {
        if (position in items.indices) {
            val currentItem = items[position]

            // Set both itemMatch and itemMovie for data binding compatibility
            binding.itemMatch = currentItem
            binding.itemMovie = convertToMovieDetailsUIState(currentItem)
            binding.executePendingBindings()
        }
    }

    private fun convertToMovieDetailsUIState(matchItem: MatchItemUI): MovieDetailsUIState {
        return MovieDetailsUIState(
            id = matchItem.id,
            image = matchItem.posterUrl.orEmpty(),
            name = matchItem.title.orEmpty(),
            releaseDate = matchItem.releaseDateShort.orEmpty(),
            genres = matchItem.genres.orEmpty(),
            review = matchItem.voteAverage?.toInt() ?: 0,
            specialNumber = 0, // Placeholder, as runtime in minutes is not provided
            hours = extractHours(matchItem.runtimeFormatted),
            minutes = extractMinutes(matchItem.runtimeFormatted),
            voteAverage = matchItem.voteAverage?.toString() ?: "0.0",
            overview = "" // Placeholder, as overview is not provided
        )
    }

    private fun extractHours(runtime: String?): Int {
        return try {
            if (runtime.isNullOrBlank()) return 0
            val hoursPart = runtime.substringBefore("h").trim()
            hoursPart.toIntOrNull() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun extractMinutes(runtime: String?): Int {
        return try {
            if (runtime.isNullOrBlank()) return 0
            val minutesPart = runtime.substringAfter("h").substringBefore("m").trim()
            minutesPart.toIntOrNull() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun setupViewPagerAppearance() {
        binding.poster.post {
            try {
                val pager = binding.poster
                val recyclerView = pager.getChildAt(0) as? RecyclerView ?: return@post

                setupViewPagerPadding(pager, recyclerView)
                setupPageTransformation(pager)
                setInitialPage(pager)

            } catch (e: Exception) {
                // Log error or handle gracefully
                e.printStackTrace()
            }
        }
    }

    private fun setupViewPagerPadding(pager: ViewPager2, recyclerView: RecyclerView) {
        val pageWidthFraction = 0.74f
        val pageWidth = (pager.width * pageWidthFraction).toInt()
        val sidePadding = (pager.width - pageWidth) / 2

        recyclerView.apply {
            setPadding(sidePadding, 0, sidePadding, 0)
            clipToPadding = false
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        pager.offscreenPageLimit = 3
    }

    private fun setupPageTransformation(pager: ViewPager2) {
        pager.setPageTransformer { page, position ->
            val absPosition = kotlin.math.abs(position)
            val scale = 0.90f + (1f - absPosition) * 0.10f
            val alpha = 0.80f + (1f - absPosition) * 0.20f

            page.apply {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
        }
    }

    private fun setInitialPage(pager: ViewPager2) {
        val middlePosition = items.size / 2
        pager.setCurrentItem(middlePosition, false)

        updateCurrentItemBinding(middlePosition)
    }

    private fun showEmptyState() {
        Toast.makeText(requireContext(), "No matches found", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentItem(): MatchItemUI? {
        return try {
            val currentPosition = binding.poster.currentItem
            if (currentPosition in items.indices) {
                items[currentPosition]
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // DetailInteractionListener implementation - these are called from data binding
    override fun onClickSave() {
        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickSaveEvent) }
    }

    override fun onClickPlayTrailer() {
        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickPlayTrailerEvent) }
    }

    override fun onclickBack() {
        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickBackEvent) }
    }

    override fun onclickViewReviews() {
        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickReviewsEvent) }
    }

    // Direct action methods called from callbacks
    private fun playTrailer(item: MatchItemUI) {
        // TODO: Implement actual trailer opening logic
        Toast.makeText(requireContext(), "Playing trailer for: ${item.title}", Toast.LENGTH_SHORT).show()
    }

    private fun saveMovie(item: MatchItemUI) {
        // TODO: Implement actual save logic
        Toast.makeText(requireContext(), "Saved: ${item.title}", Toast.LENGTH_SHORT).show()
    }

    // Feedback methods for UI events
    private fun showTrailerFeedback(item: MatchItemUI) {
        Toast.makeText(requireContext(), "Opening trailer for: ${item.title}", Toast.LENGTH_SHORT).show()
    }

    private fun showSaveFeedback(item: MatchItemUI) {
        Toast.makeText(requireContext(), "Movie saved: ${item.title}", Toast.LENGTH_SHORT).show()
    }

    private fun showReviewsFeedback(item: MatchItemUI) {
        Toast.makeText(requireContext(), "Opening reviews for: ${item.title}", Toast.LENGTH_SHORT).show()
    }

    private fun navigateBack() {
        try {
            findNavController().navigateUp()
        } catch (e: Exception) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        try {
            // TODO: Replace with actual navigation action
            // findNavController().navigate(
            //     MatchResultsFragmentDirections.actionMatchResultsToMovieDetails(movieId)
            // )
            Toast.makeText(requireContext(), "Opening details for movie ID: $movieId", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Handle navigation error
            e.printStackTrace()
        }
    }

    private fun navigateToCastDetails(castId: Int) {
        try {
            // TODO: Navigate to cast details screen
            Toast.makeText(requireContext(), "Opening cast details for ID: $castId", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Handle navigation error
            e.printStackTrace()
        }
    }

    // Public method for external movie click handling
    fun onClickMovie(movieId: Int) {
        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickMovieEvent(movieId)) }
    }

    override fun onDestroyView() {
        // Proper cleanup to prevent memory leaks
        binding.poster.unregisterOnPageChangeCallback(pageChangeCallback)
        binding.poster.adapter = null
        _binding = null
        super.onDestroyView()
    }
}