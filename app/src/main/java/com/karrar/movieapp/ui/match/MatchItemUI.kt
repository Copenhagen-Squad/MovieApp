package com.karrar.movieapp.ui.match

import android.util.Log
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.MovieDetailsUIState

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
) {
    init {
        // Debug logging for all fields
        Log.d("MatchItemUI_Debug", "=== Creating MatchItemUI ===")
        Log.d("MatchItemUI_Debug", "ID: $id")
        Log.d("MatchItemUI_Debug", "Title: '$title'")
        Log.d("MatchItemUI_Debug", "PosterUrl: '$posterUrl'")
        Log.d("MatchItemUI_Debug", "BackdropUrl: '$backdropUrl'")
        Log.d("MatchItemUI_Debug", "Genres: '$genres' (isEmpty: ${genres.isNullOrEmpty()}) (isBlank: ${genres.isNullOrBlank()})")
        Log.d("MatchItemUI_Debug", "VoteAverage: $voteAverage")
        Log.d("MatchItemUI_Debug", "RuntimeFormatted: '$runtimeFormatted' (isEmpty: ${runtimeFormatted.isNullOrEmpty()}) (isBlank: ${runtimeFormatted.isNullOrBlank()})")
        Log.d("MatchItemUI_Debug", "ReleaseDateShort: '$releaseDateShort'")
        Log.d("MatchItemUI_Debug", "IsMovie: $isMovie")
        Log.d("MatchItemUI_Debug", "===============================")

        // Additional specific checks for problematic fields
        if (genres.isNullOrEmpty()) {
            Log.w("MatchItemUI_Debug", "⚠️ GENRES IS NULL OR EMPTY for movie: $title (ID: $id)")
        }

        if (runtimeFormatted.isNullOrEmpty()) {
            Log.w("MatchItemUI_Debug", "⚠️ RUNTIME IS NULL OR EMPTY for movie: $title (ID: $id)")
        }

        // Check for whitespace-only strings
        if (genres?.isBlank() == true) {
            Log.w("MatchItemUI_Debug", "⚠️ GENRES IS BLANK (whitespace only) for movie: $title (ID: $id)")
        }

        if (runtimeFormatted?.isBlank() == true) {
            Log.w("MatchItemUI_Debug", "⚠️ RUNTIME IS BLANK (whitespace only) for movie: $title (ID: $id)")
        }
    }
}
interface MatchResultCallbacks {

    /**
     * Called when user wants to play movie trailer
     */
    fun onPlay(item: MovieDetailsUIState) {
        Log.d("MatchResultCallbacks", "onPlay called for: ${item.name} (ID: ${item.id})")
    }

    /**
     * Called when user wants to save/bookmark a movie
     */
    fun onSave(item: MovieDetailsUIState) {
        Log.d("MatchResultCallbacks", "onSave called for: ${item.name} (ID: ${item.id})")
    }

    /**
     * Called when user wants to view movie details
     */
    fun onDetails(item: MovieDetailsUIState) {
        Log.d("MatchResultCallbacks", "onDetails called for: ${item.name} (ID: ${item.id})")
    }

    /**
     * Alternative method for viewing details
     */
    fun onViewDetails(item: MovieDetailsUIState) {
        Log.d("MatchResultCallbacks", "onViewDetails called for: ${item.name} (ID: ${item.id})")
    }
}