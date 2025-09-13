package com.karrar.movieapp.data.data_source.local


import com.karrar.movieapp.data.remote.response.genre.GenreResponse
import com.karrar.movieapp.data.utils.GENRE_MOVIE_LIST
import com.karrar.movieapp.data.utils.GENRE_SERIES_LIST
import retrofit2.Response
import retrofit2.http.GET

interface GenreService {
    @GET(GENRE_MOVIE_LIST)
    suspend fun getMoviesGenres(): Response<GenreResponse>

    @GET(GENRE_SERIES_LIST)
    suspend fun getSeriesGenres(): Response<GenreResponse>
}
