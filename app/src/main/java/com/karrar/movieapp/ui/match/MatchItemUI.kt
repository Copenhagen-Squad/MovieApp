package com.karrar.movieapp.ui.match

data class MatchItemUI(
    val id: Int,
    val title: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val genres: String?,
    val voteAverage: Double?,
    val runtimeFormatted: String?,
    val releaseDateShort: String?,
    val isMovie: Boolean
)

interface MatchResultCallbacks {
    fun onPlay(item: MatchItemUI)
    fun onSave(item: MatchItemUI)
    fun onDetails(item: MatchItemUI)
    fun onViewDetails(item: MatchItemUI)
}