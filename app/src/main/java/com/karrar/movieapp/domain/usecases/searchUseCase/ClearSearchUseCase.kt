package com.karrar.movieapp.domain.usecases.searchUseCase

import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.mappers.search.SearchActorMapper
import javax.inject.Inject

class ClearSearchUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke() {
        movieRepository.clearSearchHistory()
    }
}