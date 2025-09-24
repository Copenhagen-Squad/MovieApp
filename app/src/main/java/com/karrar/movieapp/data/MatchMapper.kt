package com.karrar.movieapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.karrar.movieapp.ui.match.DurationUiState
import com.karrar.movieapp.ui.mappers.MatchUiState
import com.karrar.movieapp.ui.match.MovieScreenState
import java.time.LocalDate

object MatchMapper {
    private val moodToMovieGenres: Map<String, List<Int>> = mapOf(
        "Chill" to listOf(35, 16, 10751),      // Comedy, Animation, Family
        "Excited" to listOf(28, 12, 878),      // Action, Adventure, Sci-Fi
        "Emotional" to listOf(18, 10749),      // Drama, Romance
        "Curious" to listOf(99, 9648)          // Documentary, Mystery
    )

    private val genreToMovieGenres: Map<String, Int> = mapOf(
        "Action" to 28,
        "Comedy" to 35,
        "Drama" to 18,
        "Romance" to 10749,
        "Sci-Fi" to 878,
        "Thriller" to 53,
        "Animation" to 16,
        "Mystery" to 9648
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
                "Short" -> runtimeLte = 90
                "Medium" -> {
                    runtimeGte = 90; runtimeLte = 120
                }

                "Long" -> runtimeGte = 120
            }
        }

        var releaseDateGte: String? = null
        var releaseDateLte: String? = null
        uiState.selectedMovieTypeQuestion.firstOrNull()?.name?.let { type ->
            when (type) {
                "Recent" -> releaseDateGte = "2023-01-01"
                "Classic" -> releaseDateLte = "2000-01-01"
                "Both" -> {}
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

    fun toMatchParamsFromChoice(
        moodNames: List<String>,
        genreNames: List<String>,
        timeNames: List<String>,
        periodNames: List<String>,
    ): MatchParams {
        val moodGenres = moodNames.flatMap { moodToMovieGenres[it] ?: emptyList() }
        val selectedGenres = genreNames.mapNotNull { genreToMovieGenres[it] }
        val allGenres = (moodGenres + selectedGenres).distinct().joinToString("|")

        var runtimeGte: Int? = null
        var runtimeLte: Int? = null
        timeNames.firstOrNull()?.let { time ->
            when (time) {
                "Short" -> runtimeLte = 90
                "Medium" -> { runtimeGte = 90; runtimeLte = 120 }
                "Long" -> runtimeGte = 120
            }
        }

        var releaseDateGte: String? = null
        var releaseDateLte: String? = null
        periodNames.firstOrNull()?.let { type ->
            when (type) {
                "Recent" -> releaseDateGte = "2023-01-01"
                "Classic" -> releaseDateLte = "2000-01-01"
                "Both" -> {}
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
}

data class MatchParams(
    val genres: String?,
    val runtimeGte: Int?,
    val runtimeLte: Int?,
    val releaseDateGte: String?,
    val releaseDateLte: String?
)
