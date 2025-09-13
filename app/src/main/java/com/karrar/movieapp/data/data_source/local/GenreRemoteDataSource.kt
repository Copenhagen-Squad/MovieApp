package com.karrar.movieapp.data.data_source.local

import com.karrar.movieapp.data.remote.response.genre.GenreResponse


interface GenreRemoteDataSource {
    suspend fun getMoviesGenres(): GenreResponse
    suspend fun getSeriesGenres(): GenreResponse
}