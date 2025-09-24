package com.karrar.movieapp.ui.movieDetails.movieDetailsUIState

data class MovieDetailsUIState(
    val id: Int = 0,
    val image: String = "",
    val name: String = "",
    val releaseDate: String = "",
    val genres: String = "",
    val review: Int = 0,
    val specialNumber: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val voteAverage: String = "",
    val overview: String = "",
) {
    fun getVoteAverageFormatted(): String {
        return voteAverage.toDoubleOrNull()?.let {
            String.format("%.1f", it)
        } ?: voteAverage
    }


    val runtimeFormatted: String
        get() {
            return when {
                hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
                hours > 0 -> "${hours}h"
                minutes > 0 -> "${minutes}m"
                else -> "N/A"
            }
        }
}
