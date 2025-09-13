package com.karrar.movieapp.data.data_source.local


import com.karrar.movieapp.data.remote.response.genre.GenreResponse
import com.karrar.movieapp.data.utils.handleApi
import javax.inject.Inject

class GenreRemoteDataSourceImpl  @Inject constructor(
    private val genreService: GenreService
) : GenreRemoteDataSource {
    override suspend fun getMoviesGenres(): GenreResponse =
        handleApi {
            genreService.getMoviesGenres()
        }

    override suspend fun getSeriesGenres(): GenreResponse =
        handleApi {
            genreService.getSeriesGenres()
        }
}