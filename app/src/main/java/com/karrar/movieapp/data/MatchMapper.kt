package com.karrar.movieapp.data

import com.karrar.movieapp.ui.match.DurationUiState
import com.karrar.movieapp.ui.match.ExploreScreenState
import com.karrar.movieapp.ui.match.MatchUiState
import com.karrar.movieapp.ui.match.MovieScreenState
import com.moscow.domain.model.Movie
import java.util.Locale


object MatchMapper {

    private val moodToMovieGenres: Map<String, List<Int>> = mapOf(
        "mood_chill" to listOf(35, 16, 10751),      // Comedy, Animation, Family
        "mood_excited" to listOf(28, 12, 878),      // Action, Adventure, Sci-Fi
        "mood_emotional" to listOf(18, 10749),      // Drama, Romance
        "mood_curious" to listOf(99, 9648)          // Documentary, Mystery
    )

    private val genreToMovieGenres: Map<String, Int> = mapOf(
        "genre_action" to 28,
        "genre_comedy" to 35,
        "genre_drama" to 18,
        "genre_romance" to 10749,
        "genre_scifi" to 878,
        "genre_thriller" to 53,
        "genre_animation" to 16,
        "genre_mystery" to 9648
    )

    fun toMatchParams(uiState: MatchUiState): MatchParams {
        val moodGenres = uiState.selectedMoodQuestions
            .flatMap { moodToMovieGenres[it.name] ?: emptyList() }

        val selectedGenres = uiState.selectedGenres
            .mapNotNull { genreToMovieGenres[it.name] }

        val allGenres = (moodGenres + selectedGenres)
            .distinct()
            .joinToString("|")

        var runtimeGte: Int? = null
        var runtimeLte: Int? = null
        uiState.selectedTimeQuestion.firstOrNull()?.name?.let { time ->
            when (time) {
                "time_short_label" -> runtimeLte = 90
                "time_medium_label" -> {
                    runtimeGte = 90; runtimeLte = 120
                }

                "time_long_label" -> runtimeGte = 120
            }
        }

        var releaseDateGte: String? = null
        var releaseDateLte: String? = null
        uiState.selectedMovieTypeQuestion.firstOrNull()?.name?.let { type ->
            when (type) {
                "recent" -> releaseDateGte = "2023-01-01"
                "classic" -> releaseDateLte = "2000-01-01"
                "both" -> {}
            }
        }

        return MatchParams(
            genres = allGenres.ifEmpty { null },
            runtimeGte = runtimeGte,
            runtimeLte = runtimeLte,
            releaseDateGte = releaseDateGte,
            releaseDateLte = releaseDateLte
        )
    }

    fun toUiState(
        movie: Movie,
        genres: List<ExploreScreenState.GenreUiState>
    ): MovieScreenState.MovieDetailsUiState {
        return MovieScreenState.MovieDetailsUiState(
            id = movie.id,
            title = movie.title,
            trailerUrl = movie.trailerUrl,
            posterUrl = movie.posterUrl,
            rating = String.format(Locale.getDefault(),"%.1f", movie.rating),
            genres = if (genres.isEmpty()) emptyList() else
                movie.genreIds.map { it -> genres.first { genre -> genre.id == it }.name },
            releaseDate = movie.releaseDate,
            duration = DurationUiState(0, 0),
            description = movie.overview
        )
    }



}

data class MatchParams(
    val genres: String?,
    val runtimeGte: Int?,
    val runtimeLte: Int?,
    val releaseDateGte: String?,
    val releaseDateLte: String?
)
