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
    suspend fun getMovieDetails(id: Int): MovieDetailDto
    suspend fun rateMovie(rating: RatingRequestDto, id: Int)
    suspend fun deleteRatingMovie(movieId: Int)
    suspend fun getRatedMovies(userId: Int, page: Int): ApiResponse<RatedMovieDto>
    suspend fun getUserRatingForMovie(movieId: Int) : UserRatingResponse
    suspend fun getMovieReviews(id: Int, page: Int): ApiResponse<ReviewDto>
    suspend fun getMoviesRecommendations(id: Int, page: Int): ApiResponse<MovieDto>
    suspend fun getMoviesByGenreId(genreId: Int, page: Int): ApiResponse<MovieDto>
    suspend fun getTrendingMovies(): ApiResponse<MovieDto>
    suspend fun getUpComingMovies(page: Int): ApiResponse<MovieDto>
    suspend fun getRecentlyReleasedMovies(page: Int): ApiResponse<MovieDto>
    suspend fun getMatchYourVibeMovies(genreId: Int, page: Int): ApiResponse<MovieDto>

    suspend fun getMatchedMovies(
        page: Int,
        genres: String?,
        runtimeGte: Int?,
        runtimeLte: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?
    ): ApiResponse<MovieDto>
}