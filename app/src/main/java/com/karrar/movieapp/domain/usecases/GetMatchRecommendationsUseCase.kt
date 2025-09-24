package com.karrar.movieapp.domain.usecases

import android.util.Log
import com.karrar.movieapp.data.MatchParams
import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.mappers.movie.MovieDetailsMapper
import com.karrar.movieapp.domain.models.MovieDetails
import com.karrar.movieapp.ui.match.MatchItemUI
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetMatchRecommendationsUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val movieDetailsMapper: MovieDetailsMapper
) {
    suspend operator fun invoke(params: MatchParams): Result<List<MatchItemUI>> {
        return try {
            Log.d("GetMatchRecommendations", "Fetching basic recommendations...")
            val movies = repository.getMatchRecommendations(params)
            Log.d("GetMatchRecommendations", "Received ${movies.size} basic movie recommendations")
            Result.success(movies)
        } catch (e: Exception) {
            Log.e("GetMatchRecommendations", "Error fetching recommendations", e)
            Result.failure(e)
        }
    }

    /**
     * Gets detailed movie information for each recommended movie
     * This method fetches full details for all movies in parallel for better performance
     */
    suspend fun getDetailedRecommendations(params: MatchParams): Result<List<MovieDetails>> {
        return try {
            Log.d("GetMatchRecommendations", "Fetching basic recommendations first...")
            val basicMovies = repository.getMatchRecommendations(params)
            Log.d(
                "GetMatchRecommendations",
                "Received ${basicMovies.size} basic recommendations, now fetching details..."
            )

            // Fetch details for all movies in parallel
            val detailedMovies = coroutineScope {
                basicMovies.map { movie ->
                    async {
                        try {
                            Log.d(
                                "GetMatchRecommendations",
                                "Fetching details for movie ID: ${movie.id}"
                            )
                            getMovieDetails(movie.id)
                        } catch (e: Exception) {
                            Log.w(
                                "GetMatchRecommendations",
                                "Failed to fetch details for movie ${movie.id}: ${e.message}"
                            )
                            null // Return null for failed requests
                        }
                    }
                }.awaitAll().filterNotNull() // Remove any null results from failed requests
            }

            Log.d(
                "GetMatchRecommendations",
                "Successfully fetched details for ${detailedMovies.size} out of ${basicMovies.size} movies"
            )
            Result.success(detailedMovies)
        } catch (e: Exception) {
            Log.e("GetMatchRecommendations", "Error fetching detailed recommendations", e)
            Result.failure(e)
        }
    }

    /**
     * Alternative method that enriches the basic MovieEntity objects with available details
     * This might be more efficient if you don't need all the MovieDetails fields
     */
    suspend fun getEnrichedRecommendations(params: MatchParams): Result<List<MatchItemUI>> {
        return try {
            Log.d("GetMatchRecommendations", "Fetching basic recommendations...")
            val basicMovies = repository.getMatchRecommendations(params)

            Log.d(
                "GetMatchRecommendations",
                "Enriching ${basicMovies.size} movies with additional details..."
            )

            val enrichedMovies = coroutineScope {
                basicMovies.map { movie ->
                    async {
                        try {
                            Log.d(
                                "GetMatchRecommendations",
                                "Fetching details for movie: ${movie.title} (ID: ${movie.id})"
                            )
                            val details = repository.getMovieDetails(movie.id)
                            val enrichedMovie = movie.copy(
                                posterUrl = details?.posterPath,
                                backdropUrl = details?.backdropPath,
                                voteAverage = details?.voteAverage,
                                releaseDateShort = details?.releaseDate.toString()
                            )
                            Log.d("GetMatchRecommendations", "Successfully enriched ${movie.title}")
                            enrichedMovie
                        } catch (e: Exception) {
                            Log.w(
                                "GetMatchRecommendations",
                                "Failed to enrich movie ${movie.title} (${movie.id}): ${e.message}"
                            )
                            movie
                        }
                    }
                }.awaitAll()
            }

            Log.d(
                "GetMatchRecommendations",
                "Successfully enriched ${enrichedMovies.size} movie recommendations"
            )
            Result.success(enrichedMovies)
        } catch (e: Exception) {
            Log.e("GetMatchRecommendations", "Error enriching recommendations", e)
            Result.failure(e)
        }
    }

    private suspend fun getMovieDetails(movieId: Int): MovieDetails {
        val response = repository.getMovieDetails(movieId)
        return response?.let {
            movieDetailsMapper.map(response)
        } ?: throw Exception("Failed to fetch movie details for ID: $movieId")
    }
}