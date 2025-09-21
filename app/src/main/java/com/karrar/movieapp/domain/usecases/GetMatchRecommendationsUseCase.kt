package com.karrar.movieapp.domain.usecases

import com.karrar.movieapp.data.MatchParams
import com.karrar.movieapp.data.MovieEntity
import com.karrar.movieapp.data.repository.MovieRepository
import javax.inject.Inject

class GetMatchRecommendationsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(params: MatchParams): Result<List<MovieEntity>> {
        return try {
            val movies = repository.getMatchRecommendations(params)
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}