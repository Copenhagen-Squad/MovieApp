package com.karrar.movieapp.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.R
import com.karrar.movieapp.data.MatchMapper
import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.MatchEvent
import com.karrar.movieapp.domain.usecases.GetMatchRecommendationsUseCase
import com.karrar.movieapp.ui.mappers.MatchUiState
import com.karrar.movieapp.ui.movieDetails.mapper.MovieDetailsUIStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchResultsViewModel @Inject constructor(
    private val getMatchRecommendationsUseCase: GetMatchRecommendationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchResultsUiState())
    val uiState: StateFlow<MatchResultsUiState> = _uiState.asStateFlow()

    private val _matchResults = MutableStateFlow<List<MatchItemUI>>(emptyList())
    val matchResults: StateFlow<List<MatchItemUI>> = _matchResults.asStateFlow()

    private val _events = MutableStateFlow<MatchResultsEvent?>(null)
    val events: StateFlow<MatchResultsEvent?> = _events.asStateFlow()

    fun loadRecommendations(matchUiState: MatchUiState) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val matchParams = MatchMapper.toMatchParams(matchUiState)

            getMatchRecommendationsUseCase(matchParams)
                .onSuccess { movies ->
                    val matchItems = movies.map { MovieDetailsUIStateMapper().toMatchItemUI(it) }
                    _matchResults.update { matchItems }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasResults = matchItems.isNotEmpty(),
                            currentIndex = if (matchItems.isNotEmpty()) matchItems.size / 2 else 0
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasResults = false,
                            errorMessage = "Failed to load recommendations. Please try again."
                        )
                    }
                }
        }
    }

data class MatchResultsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val hasResults: Boolean = false,
    val currentIndex: Int = 0,
    val errorMessage: String? = null
)

sealed class MatchResultsEvent {
    data class NavigateToMovieDetails(val movieId: Int) : MatchResultsEvent()
    data class OpenTrailer(val url: String) : MatchResultsEvent()
    data class ShowMessage(val message: String) : MatchResultsEvent()
}}