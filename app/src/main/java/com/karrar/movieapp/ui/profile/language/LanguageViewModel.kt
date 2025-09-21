package com.karrar.movieapp.ui.profile.language

import androidx.lifecycle.ViewModel
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LanguageViewModel @Inject constructor() : ViewModel() {
    private val _languageUIEvent = MutableStateFlow(Event<LanguageUIEvent?>(null))
    val languageUIEvent = _languageUIEvent

    fun onCloseDialog() {
        _languageUIEvent.update { Event(LanguageUIEvent.OnCloseDialog) }
    }
}