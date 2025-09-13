package com.karrar.movieapp.data.repository

import com.karrar.movieapp.domain.models.Review
import com.moscow.domain.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun deleteRatingMovie(movieId: Int)
    suspend fun getUserRatingMovie(movieId: Int): Int
    suspend fun getRecommendationsMovie(id: Int, page: Int): List<Movie>

    suspend fun getMatchedMovies(
        page: Int,
        genres: String?,
        runtimeGte: Int?,
        runtimeLte: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?
    ): List<Movie>
}