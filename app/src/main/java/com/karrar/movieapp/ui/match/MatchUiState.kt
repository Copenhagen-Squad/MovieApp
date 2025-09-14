package com.karrar.movieapp.ui.match

data class MatchUiState(
    val isLoading: Boolean = false,
    val enableBlur: Boolean = false,
    val errorMessage: String? = null,
    val shouldShowError: Boolean = false,
    val currentPage: MatchPages = MatchPages.StartPage,
    val currentQuestionType: QuestionType = QuestionType.MOOD,
    val isLoadingRecommendations: Boolean = false,
    val isNextButtonActivated: Boolean = false,

    // Genre related
    val movieGenre: List<GenreUiState> = emptyList(),

    // Questions
    val moodQuestions: List<QuestionUiState> = emptyList(),
    val genreQuestions: List<QuestionUiState> = emptyList(),
    val timeQuestions: List<QuestionUiState> = emptyList(),
    val movieTypeQuestions: List<QuestionUiState> = emptyList(),

    // Match results
    val matchResults: List<MovieUiState> = emptyList()
) {
    // Nested data classes
    data class GenreUiState(
        val id: Int,
        val name: String,
        val isSelected: Boolean = false
    )

    data class MovieUiState(
        val id: Int,
        val title: String,
        val overview: String = "",
        val posterPath: String = "",
        val backdropPath: String = "",
        val releaseDate: String = "",
        val voteAverage: Double = 0.0,
        val genres: List<String> = emptyList()
    )
}

data class QuestionUiState(
    val id: Int,
    val text: String,
    val isSelected: Boolean = false
)

enum class QuestionType {
    MOOD,
    GENRE,
    TIME,
    TYPE
}

