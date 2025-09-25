package com.karrar.movieapp.ui.profile.myratings

import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.models.Rated
import com.karrar.movieapp.domain.usecases.GetGenreListUseCase
import com.karrar.movieapp.domain.usecases.GetListOfRatedUseCase
import com.karrar.movieapp.domain.usecases.movieDetails.GetMovieDetailsUseCase
import com.karrar.movieapp.domain.usecases.tvShowDetails.GetTvShowDetailsUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.Event
import com.karrar.movieapp.utilities.formatRuntime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRatingsViewModel @Inject constructor(
    private val getRatedUseCase: GetListOfRatedUseCase,
    private val ratedUIStateMapper: RatedUIStateMapper,
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase
) : BaseViewModel(), RatedMoviesInteractionListener {

    private val _ratedUiState = MutableStateFlow(MyRateUIState())
    val ratedUiState: StateFlow<MyRateUIState> = _ratedUiState
    private val _myRatingUIEvent: MutableStateFlow<Event<MyRatingUIEvent?>> =
        MutableStateFlow(Event(null))
    val myRatingUIEvent = _myRatingUIEvent.asStateFlow()

    private val _currentTab = MutableStateFlow(Constants.MOVIE)

    private var allRatedList: List<RatedUIState> = emptyList()


    init {
        getData()
    }

    override fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _ratedUiState.update { it.copy(isLoading = true) }
            try {
                val ratedList = getRatedUseCase()

                val movieGenresMap = getGenreListUseCase(Constants.MOVIE_CATEGORIES_ID)
                    .associateBy { it.genreID }
                val seriesGenresMap = getGenreListUseCase(Constants.TV_CATEGORIES_ID)
                    .associateBy { it.genreID }

                val ratedListWithDetails = addMovieDetailsData(ratedList)

                allRatedList = ratedListWithDetails.map { rated ->
                    val baseUi = ratedUIStateMapper.map(rated)
                    val genresNames = when (rated.mediaType) {
                        Constants.MOVIE -> rated.genres.mapNotNull { movieGenresMap[it]?.genreName }
                        Constants.TV_SHOWS -> rated.genres.mapNotNull { seriesGenresMap[it]?.genreName }
                        else -> emptyList()
                    }

                    baseUi.copy(
                        genres = genresNames,
                        duration = rated.duration
                    )
                }

                filterRatedList()
                _ratedUiState.update { it.copy(isLoading = false) }
            } catch (e: Throwable) {
                _ratedUiState.update { it.copy(error = listOf(Error("")), isLoading = false) }
            }
        }
    }

    fun selectTab(tab: String) {
        _currentTab.value = tab
        filterRatedList()
    }

    private fun filterRatedList() {
        val filtered = allRatedList.filter { it.mediaType == _currentTab.value }
        _ratedUiState.update { it.copy(ratedList = filtered) }
    }

    override fun onClickMovie(item:RatedUIState) {
        if (item.mediaType.equals(Constants.MOVIE, true)) {
            _myRatingUIEvent.update { Event(MyRatingUIEvent.MovieEvent(item.id)) }
        } else {
            _myRatingUIEvent.update { Event(MyRatingUIEvent.TVShowEvent(item.id)) }
        }
    }

    override fun onClickBack() {
        _myRatingUIEvent.update { Event(MyRatingUIEvent.BackEvent) }
    }


    private suspend fun addMovieDetailsData(ratedItems: List<Rated>): List<Rated> {
        return ratedItems.map { rate ->
            if (rate.mediaType == Constants.MOVIE) {
                val movieDetails = getMovieDetailsUseCase.getMovieDetails(rate.id)
                rate.copy(
                    duration = movieDetails.movieDuration.formatRuntime().toString(),
                )
            } else {
                val tvDetails = getTvShowDetailsUseCase.getTvShowDetails(rate.id)
                rate.copy(
                    duration = tvDetails.tvShowEpisodeRuntime.firstOrNull().formatRuntime().toString()
                )
            }
        }
    }

    fun retryConnect() {
        _ratedUiState.update { it.copy(error = emptyList()) }
        getData()
    }
}