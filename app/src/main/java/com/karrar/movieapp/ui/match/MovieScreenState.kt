package com.karrar.movieapp.ui.match

import java.time.LocalDate

data class MovieScreenState(
    val movieDetailsUiState: MovieDetailsUiState? = null,
    val reviewsFlow: List<ReviewUiState>? = null,
    val characters: List<String> = emptyList(),
    val director: List<String> = emptyList(),
    val produce: List<String> = emptyList(),
    val writer: List<String> = emptyList(),
    val recommendations: List<MediaItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isReviewEmpty: Boolean = false,
    val shouldShowLoading: Boolean = false,
    val shouldShowError: Boolean = false,
    val errorMessage: Int = 0,
    val recentlyViewedCollectionId: Int = 0,
    val showRatingBottomSheet: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val starsRating: Int = 0,
    val enableBlur: String = "high"
) {
    data class MovieDetailsUiState(
        val id: Int,
        val title: String,
        val trailerUrl: String,
        val posterUrl: String,
        val rating: String,
        val genres: List<String>,
        val releaseDate: LocalDate?,
        val duration: DurationUiState,
        val description: String
    )

    data class DurationUiState(
        val hours: Int,
        val minutes: Int
    ) {
        override fun toString(): String {
            return when {
                hours > 0 -> "${hours}h ${minutes}m"
                else -> "${minutes}m"
            }
        }
    }
}

// Define missing data classes
data class ReviewUiState(
    val id: String,
    val author: String,
    val content: String,
    val rating: Float?,
    val createdAt: String
)

data class MediaItemUiState(
    val id: Int,
    val title: String,
    val posterPath: String,
    val rating: Float,
    val genres: List<String>,
    val releaseDate: LocalDate?,
    val backdropPath: String,
    val mediaType: MediaType
) {
    enum class MediaType {
        Movie, TvShow
    }
}

fun MovieScreenState.MovieDetailsUiState.toMediaItem() =
    MediaItemUiState(
        id = this.id,
        title = this.title,
        posterPath = this.posterUrl,
        rating = this.rating.toFloatOrNull() ?: 0.0f,
        genres = this.genres,
        releaseDate = this.releaseDate,
        backdropPath = this.posterUrl,
        mediaType = MediaItemUiState.MediaType.Movie
    )