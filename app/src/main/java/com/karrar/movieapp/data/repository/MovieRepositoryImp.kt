package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.data_source.local.MovieRemoteDataSource
import com.karrar.movieapp.data.local.mappers.movie.toDomain
import com.moscow.domain.model.Movie
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): List<Movie> {
        return movieRemoteDataSource.getPopularMovies(page = page).results?.map { it.toDomain() }
            ?: emptyList()
    }


    override suspend fun deleteRatingMovie(movieId: Int) {
        movieRemoteDataSource.deleteRatingMovie(movieId)
    }


    override suspend fun getUserRatingMovie(movieId: Int): Int {
        val response = movieRemoteDataSource.getUserRatingForMovie(movieId)
        return response.userRating ?: 0
    }

    override suspend fun getRecommendationsMovie(
        id: Int,
        page: Int
    ): List<Movie> {
        val movies = movieRemoteDataSource.getMoviesRecommendations(id, page)
        return movies.results?.mapNotNull { runCatching { it.toDomain() }.getOrNull() }
            ?: emptyList()
    }


    override suspend fun getMatchedMovies(
        page: Int,
        genres: String?,
        runtimeGte: Int?,
        runtimeLte: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?
    ): List<Movie> {
        val movies = movieRemoteDataSource.getMatchedMovies(
            page,
            genres,
            runtimeGte,
            runtimeLte,
            releaseDateGte,
            releaseDateLte
        )
        return movies.results?.mapNotNull { runCatching { it.toDomain() }.getOrNull() }
            ?: emptyList()
    }
}
