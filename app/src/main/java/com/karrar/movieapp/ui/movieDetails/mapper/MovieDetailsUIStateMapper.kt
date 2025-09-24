package com.karrar.movieapp.ui.movieDetails.mapper

import com.karrar.movieapp.data.MovieEntity
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.MovieDetails
import com.karrar.movieapp.ui.match.DurationUiState
import com.karrar.movieapp.ui.match.MatchItemUI
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.MovieDetailsUIState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class MovieDuration(val hours: Int, val minutes: Int)

class MovieDetailsUIStateMapper @Inject constructor() : Mapper<MovieDetails, MovieDetailsUIState> {
    override fun map(input: MovieDetails): MovieDetailsUIState {
        val duration = formatMovieDuration(input.movieDuration)
        return MovieDetailsUIState(
            id = input.movieId,
            image = input.movieImage,
            name = input.movieName,
            releaseDate = input.movieReleaseDate,
            genres = input.movieGenres,
            hours = duration.hours,
            minutes = duration.minutes,
            specialNumber = input.movieDuration,
            review = input.movieReview,
            voteAverage = input.movieVoteAverage,
            overview = input.movieOverview,
        )
    }

    private fun formatMovieDuration(duration: Int): MovieDuration {
        return MovieDuration(hours = duration.div(60), minutes = duration.rem(60))
    }


    fun toMatchItemUI(movie: MovieEntity): MatchItemUI {
        return MatchItemUI(
            id = movie.id,
            title = movie.title,
            posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
            backdropUrl = movie.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" },
            genres = movie.genreIds?.joinToString(", ") { mapGenreIdToName(it) },
            voteAverage = movie.voteAverage,
            runtimeFormatted = formatRuntime(movie.releaseDate as Int?),
            releaseDateShort = (movie.releaseDate),
            isMovie = true
        )
    }


    private fun mapGenreIdToName(id: Int): String = when(id) {
        28 -> "Action"
        35 -> "Comedy"
        18 -> "Drama"
        10749 -> "Romance"
        878 -> "Sci-Fi"
        53 -> "Thriller"
        16 -> "Animation"
        9648 -> "Mystery"
        99 -> "Documentary"
        10751 -> "Family"
        12 -> "Adventure"
        else -> "Unknown"
    }

    private fun formatRuntime(runtime: Int?): String? {
        return runtime?.let {
            val hours = it / 60
            val minutes = it % 60
            when {
                hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
                hours > 0 -> "${hours}h"
                else -> "${minutes}m"
            }
        }
    }


    private fun parseDuration(runtime: Int?): DurationUiState {
        return DurationUiState(
            hours = (runtime ?: 0) / 60,
            minutes = (runtime ?: 0) % 60
        )
    }

}

