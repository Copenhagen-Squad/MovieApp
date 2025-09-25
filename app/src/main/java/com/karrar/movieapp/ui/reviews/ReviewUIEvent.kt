package com.karrar.movieapp.ui.reviews

sealed interface ReviewUIEvent {
    object BackEvent : ReviewUIEvent
}