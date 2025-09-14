package com.karrar.movieapp.domain.usecases.match

import com.karrar.movieapp.domain.models.Genre
import com.karrar.movieapp.ui.match.MatchUiState

fun Genre.toUi(): MatchUiState.GenreUiState {
    return MatchUiState.GenreUiState(
        id = this.genreID,
        name = this.genreName,
        isSelected = false
    )
}