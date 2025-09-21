package com.karrar.movieapp.ui.match.questions

import com.karrar.movieapp.ui.base.BaseInteractionListener

interface QuestionInteractionListener : BaseInteractionListener {

    fun onNextClicked()

    fun getCurrentQuestionType(): QuestionType
}