package com.karrar.movieapp.ui.movieDetails

import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.utilities.Event
import kotlinx.coroutines.flow.update

interface DetailInteractionListener :BaseInteractionListener {

    fun onclickBack()

//    override fun onClickSave() {
//        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickSaveEvent) }
//    }
//
//    override fun onClickPlayTrailer() {
//        _movieDetailsUIEvent.update { Event(MovieDetailsUIEvent.ClickPlayTrailerEvent) }
//    }

    fun onClickSave()

    fun onClickPlayTrailer()

    fun onclickViewReviews()
}