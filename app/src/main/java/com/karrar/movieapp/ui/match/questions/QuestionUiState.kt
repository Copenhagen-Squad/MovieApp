package com.karrar.movieapp.ui.match.questions

data class QuestionUiState (
    val currentQuestionType: QuestionType = QuestionType.MOOD,
    val moodSelected: List<Choice> = emptyList(),
    val genreSelected: List<Choice> = emptyList(),
    val mediaRuntimeSelected: List<Choice> = emptyList(),
    val timePeriodSelected: List<Choice> = emptyList(),
    val isLoading: Boolean = false,
    val progress: Int = 25,
    )