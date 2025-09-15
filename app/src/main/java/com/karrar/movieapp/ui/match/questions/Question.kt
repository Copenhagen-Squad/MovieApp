package com.karrar.movieapp.ui.match.questions

data class Question(
    val question: String,
    val type: QuestionType,
    val choices: List<Choice>,
    val isAnswered: Boolean,
)

enum class QuestionType {
    MOOD,
    GENRE,
    MEDIA_RUNTIME,
    TIME_PERIOD,
}
