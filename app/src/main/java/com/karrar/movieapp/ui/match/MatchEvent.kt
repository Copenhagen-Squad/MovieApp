package com.karrar.movieapp.ui.match

sealed class MatchEvent {
    data object OnClickStartMatching : MatchEvent()
    data object OnClickFinishMatching : MatchEvent()
    data class OnMovieClick(val id: Int): MatchEvent()
    data class OpenTrailer(val url: Int) : MatchEvent()
    data class AddToCollection(val id: Int): MatchEvent()
}
