package com.karrar.movieapp.data.local.mappers.movie

import com.karrar.movieapp.data.remote.response.MovieDto
import com.moscow.domain.model.Movie
import java.time.LocalDate

const val IMAGES_URL = "https://image.tmdb.org/t/p/w500/"

fun MovieDto.toDomain() =
    Movie(
        overview = overview.orEmpty(),
        genreIds = genreIds ?: emptyList(),
        id = id ?: 0,
        title = title.orEmpty(),
        genres = emptyList(),
        duration = Movie.Duration(0, 0),
        rating = voteAverage ?: 0.0f,
        trailerUrl = "",
        posterUrl = IMAGES_URL + posterPath.orEmpty(),
        backdropUrl = IMAGES_URL + backdropPath.orEmpty(),
        releaseDate = if (!releaseDate.isNullOrBlank()) LocalDate.parse(releaseDate) else null,
    )