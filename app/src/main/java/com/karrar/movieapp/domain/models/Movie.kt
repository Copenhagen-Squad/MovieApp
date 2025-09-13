package com.moscow.domain.model

import java.time.LocalDate

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val trailerUrl: String,
    val backdropUrl: String,
    val posterUrl: String,
    val releaseDate: LocalDate?,
    val genreIds: List<Int>,
    val genres: List<String>,
    val duration: Duration,
    val rating: Any
){
    data class Duration(
        val hours: Int,
        val minutes: Int
    )
}