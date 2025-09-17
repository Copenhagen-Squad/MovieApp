package com.karrar.movieapp.ui.match

data class MatchItemUI(
    val id: Int,
    val title: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val genres: String?,            // "Drama, Action, Thriller"
    val voteAverage: Double?,       // 8.5
    val runtimeFormatted: String?,  // "2h 32m"
    val releaseDateShort: String?   // "2008, Jul 18"
)

interface MatchResultCallbacks {
    fun onPlay(item: MatchItemUI)
    fun onSave(item: MatchItemUI)
    fun onDetails(item: MatchItemUI)
}