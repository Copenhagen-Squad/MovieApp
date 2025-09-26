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

    private val _ratingState = MutableStateFlow(0f)
    val ratingState = _ratingState.asStateFlow()

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
                    _ratingState.value = currentRating
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun onChangeRating(value: Float) {
        viewModelScope.launch {
            try {
                setRatingUseCase(args.movieId, value)
                _ratingState.value = value
                _ratingEvent.value = Event(RatingEvent.RatingSuccess)
            } catch (e: Exception) {
                _ratingEvent.value = Event(RatingEvent.RatingError(e.message))
            }
        }
    }
}

sealed class RatingEvent {
    object RatingSuccess : RatingEvent()
    data class RatingError(val message: String?) : RatingEvent()
}
