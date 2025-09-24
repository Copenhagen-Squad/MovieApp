package com.karrar.movieapp.ui.profile

sealed interface ProfileUIEvent {
    object LoginEvent : ProfileUIEvent
    object RatedMoviesEvent : ProfileUIEvent
    object DialogLogoutEvent : ProfileUIEvent
    object WatchHistoryEvent : ProfileUIEvent
    object MyCollectionEvent : ProfileUIEvent
    object OnClickChangeLanguage : ProfileUIEvent
    object OnClickContentPreferences : ProfileUIEvent
    object OnClickEditProfile : ProfileUIEvent
}
