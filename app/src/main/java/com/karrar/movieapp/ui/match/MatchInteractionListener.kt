package com.karrar.movieapp.ui.match

import com.karrar.movieapp.domain.QuestionType

interface MatchInteractionListener {
    fun onClickStartMatching()
    fun onClickFinishMatching()
    fun onClickNextQuestion()
    fun onAnswerSelected(type: QuestionType, answer: QuestionUiState)
    fun onNavigateBack()
    fun onMovieClick(id: Int)
    fun onSaveClick(id: Int)
    fun onRetry()
}