package com.karrar.movieapp.domain.enums

import androidx.annotation.StringRes
import com.karrar.movieapp.R

enum class HomeItemsType(@StringRes val titleRes: Int) {
    TOP_RATED_TV_SHOWS(R.string.top_rated_tv_shows),
    RECENTLY_RELEASED(R.string.recently_released),
    UPCOMING(R.string.upcoming_movies),
    RECENTLY_VIEWED(R.string.recently_viewed),
    YOUR_COLLECTIONS(R.string.your_collections),
    MATCH_YOUR_VIBE(R.string.match_your_vibe),
    FEATURED_COLLECTIONS(R.string.featured_collections),
    NON(R.string.empty_string)
}