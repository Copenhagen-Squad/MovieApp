package com.karrar.movieapp.ui.home.homeUiState

import com.karrar.movieapp.ui.home.HomeItem

data class HomeUiState (
    val popularMovies: HomeItem = HomeItem.Slider(emptyList()),
    val recentlyReleasedMovies: HomeItem = HomeItem.RecentlyReleased(emptyList()),
    val upcomingMovies: HomeItem = HomeItem.Upcoming(emptyList()),
    val matchYourVibe: HomeItem = HomeItem.MatchYourVibe(emptyList()),
    val onTheAiringSeries: HomeItem = HomeItem.OnTheAiring(emptyList()),
    val browseEverything: HomeItem = HomeItem.BrowseEverything(),
    val letUsChooseForYou: HomeItem = HomeItem.LetUsChooseForYou(),
    val recentlyViewed: HomeItem = HomeItem.RecentlyViewed(emptyList()),
    val collections: HomeItem = HomeItem.CollectionsList(emptyList()),
    val featured: HomeItem = HomeItem.FeaturedCollections(emptyList()),
    val featuredCollections: List<FeaturedCollectionUiState> = emptyList(),
    val isLoading:Boolean = false,
    val error : List<String> = emptyList(),
    val name: String? = null,
    val homeCaption: String? = null,
    val isLoggedIn: Boolean = false
)