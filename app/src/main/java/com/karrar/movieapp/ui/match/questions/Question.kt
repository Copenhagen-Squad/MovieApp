package com.karrar.movieapp.ui.match.questions

data class Question(
    val question: String,
    val type: QuestionType,
    val choices: List<Choice>,
    val isAnswered: Boolean,
){
    val displayChoices: List<Choice>
        get() = if (isAnswered) {
            choices.filter { it.isSelected }
        } else {
            choices
        }
}

enum class QuestionType {
    MOOD,
    GENRE,
    MEDIA_RUNTIME,
    TIME_PERIOD,
}
