package com.karrar.movieapp.ui.adapters

import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.models.MoodUiState

class MoodAdapter(moods: List<MoodUiState>, layout: Int, listener: MoodInteractionListener):
    BaseAdapter<MoodUiState>(moods, listener) {
    override val layoutID: Int = layout
}

interface MoodInteractionListener : BaseInteractionListener {
    fun onClickMode(moodId: Int)
}