package com.karrar.movieapp.domain.usecases.movieDetails

import com.karrar.movieapp.data.repository.MovieRepository
import com.moscow.domain.model.Movie
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(
        id: Int
    ): List<Movie> = movieRepository.getMatchedMovies(
        page = 1,
        genres = null,
        runtimeGte = null,
        runtimeLte = null,
        releaseDateGte = null,
        releaseDateLte = null
    )
}