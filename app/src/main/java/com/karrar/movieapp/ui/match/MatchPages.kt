package com.karrar.movieapp.ui.match

sealed interface MatchPages {
    data object StartPage: MatchPages
    data object QuestionsPage: MatchPages
    data object ResultsPage: MatchPages
}