package com.karrar.movieapp.ui.match.questions

import androidx.annotation.StringRes

data class Question(
    @StringRes val question: Int,
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
