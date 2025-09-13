package com.karrar.movieapp.domain

import com.karrar.movieapp.domain.models.Genre

interface GenreRepository {
    suspend fun getSeriesGenres(forceRefresh: Boolean = false): List<Genre>
    suspend fun getMoviesGenres(forceRefresh: Boolean = false): List<Genre>
}