package com.karrar.movieapp.ui.match

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MatchResultsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MatchResultsUiState())
    val uiState = _uiState.asStateFlow()

    private val _items = MutableStateFlow<List<MatchItemUI>>(emptyList())
    val items = _items.asStateFlow()

    fun loadMatchResults() {
        // Simulate loading match results based on user preferences
        val demoResults = listOf(
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
            ),
            MatchItemUI(
                id = 4,
                title = "Inception",
                posterUrl = "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                genres = "Action, Sci-Fi, Thriller",
                voteAverage = 8.8,
                runtimeFormatted = "2h 28m",
                releaseDateShort = "2010, Jul 16",
                isMovie = true
            ),
            MatchItemUI(
                id = 5,
                title = "Interstellar",
                posterUrl = "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg",
                genres = "Drama, Sci-Fi, Adventure",
                voteAverage = 8.6,
                runtimeFormatted = "2h 49m",
                releaseDateShort = "2014, Nov 07",
                isMovie = true
            )
        )

        _items.value = demoResults
        _uiState.update { it.copy(isLoading = false) }
    }

    fun onPlayClicked(item: MatchItemUI) {
        // Handle play action
        _uiState.update { it.copy(selectedItem = item) }
    }

    fun onSaveClicked(item: MatchItemUI) {
        // Handle save action
        _uiState.update { it.copy(selectedItem = item) }
    }

    fun onViewDetailsClicked(item: MatchItemUI) {
        // Handle view details action
        _uiState.update { it.copy(selectedItem = item) }
    }
}

data class MatchResultsUiState(
    val isLoading: Boolean = true,
    val selectedItem: MatchItemUI? = null
)