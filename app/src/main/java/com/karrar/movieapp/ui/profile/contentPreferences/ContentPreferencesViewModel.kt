package com.karrar.movieapp.ui.profile.contentPreferences

import androidx.lifecycle.ViewModel
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ContentPreferencesViewModel @Inject constructor() : ViewModel() {

    private val _contentPreferencesUIEvent = MutableStateFlow(Event<ContentPreferencesUIEvent?>(null))
    val contentPreferencesUIEvent = _contentPreferencesUIEvent

    fun onCloseDialog() {
        _contentPreferencesUIEvent.update { Event(ContentPreferencesUIEvent.OnCloseDialog) }
    }
}