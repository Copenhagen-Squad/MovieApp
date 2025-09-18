package com.karrar.movieapp.ui.profile.myratings

data class MyRateUIState(
    val isLoading: Boolean = false,
    val rateMovies: List<RatedUIState> = emptyList(),
    val ratedSeries: List<RatedUIState> = emptyList(),
    val error: List<Error> = emptyList()
)