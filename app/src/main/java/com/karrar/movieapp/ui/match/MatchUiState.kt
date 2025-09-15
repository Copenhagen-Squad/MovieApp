package com.karrar.movieapp.ui.match

import androidx.annotation.DrawableRes
import com.karrar.movieapp.R
import com.karrar.movieapp.domain.QuestionType

data class MatchUiState(
    val isLoading: Boolean = false,
    val isLoadingRecommendations: Boolean = false,
    val shouldShowError: Boolean = false,
    val errorMessage: Int? = null,
    val enableBlur: String = "high",

    // Navigation
    val currentPage: MatchPages = MatchPages.StartPage,
    val currentQuestionType: QuestionType = QuestionType.MOOD,

    // Questions
    val moodQuestions: List<QuestionUiState> = getMoodQuestionAnswers(),
    val genreQuestions: List<QuestionUiState> = getGenreQuestionAnswers(),
    val timeQuestions: List<QuestionUiState> = getTimeQuestionAnswers(),
    val movieTypeQuestions: List<QuestionUiState> = getMovieTypeQuestionAnswers(),

    // Results
    val movieGenre: List<ExploreScreenState.GenreUiState> = emptyList(),
    val matchResults: List<MovieScreenState.MovieDetailsUiState> = emptyList()
) {

    // ========== Computed Properties ==========

    val matchProgress: Float =
        currentQuestionType.ordinal.plus(1).toFloat() / QuestionType.entries.size

    val isNextButtonActivated: Boolean = when (currentQuestionType) {
        QuestionType.MOOD -> moodQuestions.any { it.isSelected }
        QuestionType.GENRE -> genreQuestions.any { it.isSelected }
        QuestionType.TIME -> timeQuestions.any { it.isSelected }
        QuestionType.TYPE -> movieTypeQuestions.any { it.isSelected }
    }

    val selectedMoodQuestions: List<QuestionUiState>
        get() = moodQuestions.filter { it.isSelected }

    val selectedGenres: List<QuestionUiState>
        get() = genreQuestions.filter { it.isSelected }

    val selectedTimeQuestion: List<QuestionUiState>
        get() = timeQuestions.filter { it.isSelected }

    val selectedMovieTypeQuestion: List<QuestionUiState>
        get() = movieTypeQuestions.filter { it.isSelected }
}

data class QuestionUiState(
    val id: Int,
    val name: String,
    val description: String = "null",
    val isSelected: Boolean = false,
    @DrawableRes val iconResource: Int? = null
)


enum class QuestionType {
    MOOD,
    GENRE,
    TIME,
    TYPE
}

// ========== Question Answer Functions ==========

fun getMoodQuestionAnswers(): List<QuestionUiState> = listOf(
    QuestionUiState(
        id = 1,
        name = "Chill",
        iconResource = R.drawable.ic_headphone_duetone
    ),
    QuestionUiState(
        id = 2,
        name = "Excited",
        iconResource = R.drawable.ic_flame_duetone
    ),
    QuestionUiState(
        id = 3,
        name = "Emotional",
        iconResource = R.drawable.ic_heart_duetone
    ),
    QuestionUiState(
        id = 4,
        name = "Curious",
        iconResource = R.drawable.ic_search_duetone
    )
)

fun getGenreQuestionAnswers(): List<QuestionUiState> = listOf(
    QuestionUiState(id = 1, name = "Action"),
    QuestionUiState(id = 2, name = "Comedy"),
    QuestionUiState(id = 3, name = "Drama"),
    QuestionUiState(id = 4, name = "Romance"),
    QuestionUiState(id = 5, name = "Sci-Fi"),
    QuestionUiState(id = 6, name = "Thriller"),
    QuestionUiState(id = 7, name = "Animation"),
    QuestionUiState(id = 8, name = "Mystery")
)

fun getTimeQuestionAnswers(): List<QuestionUiState> = listOf(
    QuestionUiState(
        id = 1,
        name = "Short",
        description = "Less than 90 minutes",
        iconResource = R.drawable.ic_time_short_duetone
    ),
    QuestionUiState(
        id = 2,
        name = "Medium",
        description = "90 to 120 minutes",
        iconResource = R.drawable.ic_time_medium_duetone
    ),
    QuestionUiState(
        id = 3,
        name = "Long",
        description = "More than 120 minutes",
        iconResource = R.drawable.ic_time_long_duetone
    )
)

fun getMovieTypeQuestionAnswers(): List<QuestionUiState> = listOf(
    QuestionUiState(
        id = 1,
        name = "Recent"
    ),
    QuestionUiState(
        id = 2,
        name = "Classic"
    ),
    QuestionUiState(
        id = 3,
        name = "Both"
    )
)