package com.karrar.movieapp.ui.match

import java.time.LocalDate


data class ReviewUiState(
    val id: String,
    val name: String,
    val username: String,
    val rate: Float,
    val reviewContent: String,
    val date: LocalDate?,
    val userImageUrl: String
)