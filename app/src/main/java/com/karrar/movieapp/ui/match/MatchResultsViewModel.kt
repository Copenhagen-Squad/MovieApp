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
        genres = savedStateHandle.get<String>("genres").orEmpty().ifBlank { null },
        runtimeGte = savedStateHandle.get<Int>("runtimeGte")?.takeIf { it >= 0 },
        runtimeLte = savedStateHandle.get<Int>("runtimeLte")?.takeIf { it >= 0 },
        releaseDateGte = savedStateHandle.get<String>("releaseDateGte").orEmpty().ifBlank { null },
        releaseDateLte = savedStateHandle.get<String>("releaseDateLte").orEmpty().ifBlank { null },
    )

    init {
        fetch()
    }

    private fun fetch() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = getMatchRecommendations.getEnrichedRecommendations(args)
            result.onSuccess { list ->
                val mapped = list.map { m ->
                    MatchItemUI(
                        id = m.id,
                        title = m.title,
                        posterUrl = m.posterPath?.let { path -> "https://image.tmdb.org/t/p/w500$path" },
                        backdropUrl = m.backdropPath?.let { path -> "${Constants.IMAGE_BASE_PATH}$path" },
                        genres = m.genreIds,
                        voteAverage = m.voteAverage,
                        runtimeFormatted = m.runtime,
                        releaseDateShort = m.releaseDate,
                        isMovie = true
                    )
                }
                _items.value = mapped
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { exception ->
                android.util.Log.e("MatchResultsViewModel", "Failed to fetch recommendations: ${exception.message}")
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }
}

data class MatchResultsUiState(
    val isLoading: Boolean = true,
    val selectedItem: MatchItemUI? = null,
    val error: String? = null
)