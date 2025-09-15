package com.karrar.movieapp.domain

sealed class MatchEvent {
    data class OnMovieClick(val id: Int) : MatchEvent()
    data class AddToCollection(val id: Int) : MatchEvent()
    data class OpenTrailer(val url: String) : MatchEvent()
}