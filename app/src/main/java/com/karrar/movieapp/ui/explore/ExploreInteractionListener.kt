package com.karrar.movieapp.ui.explore

import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.explore.exploreUIState.ExploreDisplayMode
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIState

interface ExploreInteractionListener : BaseInteractionListener {
    fun onClickMedia(mediaItem: MediaUIState)
    fun onClickCategory(categoryId: Int)
    fun onClickMediaType(mediaTypeId: Int)
    fun onClickViewMode(viewMode: ExploreDisplayMode)
}