package com.karrar.movieapp.ui.match

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.data.MatchParams
import com.karrar.movieapp.domain.usecases.GetMatchRecommendationsUseCase
import com.karrar.movieapp.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MatchResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMatchRecommendations: GetMatchRecommendationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchResultsUiState())
    val uiState = _uiState.asStateFlow()

    private val _items = MutableStateFlow<List<MatchItemUI>>(emptyList())
    val items = _items.asStateFlow()

    private val args = MatchParams(
        genres = savedStateHandle.get<String>("genres")?.takeIf { it.isNotBlank() },
        runtimeGte = savedStateHandle.get<Int>("runtimeGte")?.takeIf { it >= 0 },
        runtimeLte = savedStateHandle.get<Int>("runtimeLte")?.takeIf { it >= 0 },
        releaseDateGte = savedStateHandle.get<String>("releaseDateGte")?.takeIf { it.isNotBlank() },
        releaseDateLte = savedStateHandle.get<String>("releaseDateLte")?.takeIf { it.isNotBlank() },
    )

    init {
        fetch()
    }

    private fun fetch() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val result = getMatchRecommendations.getEnrichedRecommendations(args)
                result.onSuccess { movieList ->
                    val mapped = movieList.map { movie ->
                        MatchItemUI(
                            id = movie.id?.toString()?.toIntOrNull() ?: 0,
                            title = movie.title?.toString() ?: "",
                            posterUrl = movie.posterUrl?.toString()?.let { path ->
                                if (path.startsWith("http")) path
                                else "https://image.tmdb.org/t/p/w500$path"
                            },
                            backdropUrl = movie.backdropUrl?.toString()?.let { path ->
                                if (path.startsWith("http")) path
                                else "${Constants.IMAGE_BASE_PATH}$path"
                            },
                            genres = movie.genres?.let { genresList ->
                                when (genresList) {
                                    is String -> genresList
                                    else -> genresList.toString()
                                }
                            },
                            voteAverage = movie.voteAverage?.toString()?.toDoubleOrNull(),
                            runtimeFormatted = movie.runtimeFormatted.let { runtimeStr ->
                                    runtimeStr
                            },
                            releaseDateShort = movie.releaseDateShort?.toString()?.let { dateStr ->
                                formatReleaseDate(dateStr)
                            },
                            isMovie = true
                        )
                    }
                    _items.value = mapped
                    _uiState.update { it.copy(isLoading = false, error = null) }
                }.onFailure { exception ->
                    android.util.Log.e("MatchResultsViewModel", "Failed to fetch recommendations", exception)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            } catch (exception: Exception) {
                android.util.Log.e("MatchResultsViewModel", "Error in fetch", exception)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
    private fun formatReleaseDate(dateString: String): String {
        return try {
            dateString.split("-").firstOrNull() ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }
}

data class MatchResultsUiState(
    val isLoading: Boolean = true,
    val selectedItem: MatchItemUI? = null,
    val error: String? = null
)