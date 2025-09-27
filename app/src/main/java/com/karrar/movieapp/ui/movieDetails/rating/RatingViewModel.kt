package com.karrar.movieapp.ui.movieDetails.rating

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.GetSessionIDUseCase
import com.karrar.movieapp.domain.usecases.movieDetails.GetMovieRateUseCase
import com.karrar.movieapp.domain.usecases.movieDetails.SetRatingUseCase
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val setRatingUseCase: SetRatingUseCase,
    private val getMovieRateUseCase: GetMovieRateUseCase,
    private val sessionIDUseCase: GetSessionIDUseCase,
    state: SavedStateHandle
) : ViewModel() {

    private val args = RatingBottomSheetDialogArgs.fromSavedStateHandle(state)

    private val _uiState = MutableStateFlow(RatingUiState())
    val uiState = _uiState.asStateFlow()

    private val _ratingEvent = MutableStateFlow<Event<RatingEvent?>>(Event(null))
    val ratingEvent = _ratingEvent.asStateFlow()

    init {
        loadCurrentRating()
    }

    private fun loadCurrentRating() {
        if (!sessionIDUseCase().isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val currentRating = getMovieRateUseCase(args.movieId)
                    _uiState.value = _uiState.value.copy(currentRating = currentRating)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun onChangeRating(value: Float) {
        _uiState.value = _uiState.value.copy(selectedRating = value)
    }

    fun onSubmitRating() {
        viewModelScope.launch {
            try {
                setRatingUseCase(args.movieId, _uiState.value.selectedRating)
                _ratingEvent.value = Event(RatingEvent.RatingSuccess)
            } catch (e: Exception) {
                _ratingEvent.value = Event(RatingEvent.RatingError(e.message))
            }
        }
    }

    fun onCloseDialog() {
        _ratingEvent.value = Event(RatingEvent.CloseDialog)
    }
}

data class RatingUiState(
    val currentRating: Float = 0f,
    val selectedRating: Float = 0f,
    val isLoading: Boolean = false
)

sealed class RatingEvent {
    object RatingSuccess : RatingEvent()
    object CloseDialog : RatingEvent()
    data class RatingError(val message: String?) : RatingEvent()
}