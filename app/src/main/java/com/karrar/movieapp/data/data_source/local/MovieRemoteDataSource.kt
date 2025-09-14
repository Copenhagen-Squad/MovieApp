package com.karrar.movieapp.data.data_source.local

import com.karrar.movieapp.data.remote.MovieDetailDto
import com.karrar.movieapp.data.remote.response.MovieDto
import com.karrar.movieapp.data.remote.response.RatedMovieDto
import com.karrar.movieapp.data.remote.response.RatingRequestDto
import com.karrar.movieapp.data.remote.response.ReviewDto
import com.karrar.movieapp.data.remote.response.UserRatingResponse
import com.karrar.movieapp.data.utils.ApiResponse

interface MovieRemoteDataSource {
    suspend fun getPopularMovies(page: Int): ApiResponse<MovieDto>
    suspend fun deleteRatingMovie(movieId: Int)
    suspend fun getUserRatingForMovie(movieId: Int) : UserRatingResponse
    suspend fun getMoviesRecommendations(id: Int, page: Int): ApiResponse<MovieDto>

    suspend fun getMatchedMovies(
        page: Int,
        genres: String?,
        runtimeGte: Int?,
        runtimeLte: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?
    ): ApiResponse<MovieDto>
}