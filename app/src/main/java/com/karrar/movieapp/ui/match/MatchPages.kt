package com.karrar.movieapp.ui.match

sealed interface MatchPages {
    object StartPage : MatchPages
    object QuestionsPage : MatchPages
    object ResultsPage : MatchPages
}