package com.karrar.movieapp.domain.usecases

import com.moscow.domain.repository.GenreRepository
import javax.inject.Inject

class GenreUseCase @Inject constructor(
    private val genreRepository: GenreRepository
) {
    suspend fun getMoviesGenres(refresh: Boolean = false) =
        genreRepository.getMoviesGenres(forceRefresh = refresh)

    suspend fun getSeriesGenres(refresh: Boolean = false) =
        genreRepository.getSeriesGenres(forceRefresh = refresh)
}