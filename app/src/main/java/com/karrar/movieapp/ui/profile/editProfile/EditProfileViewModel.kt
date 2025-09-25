package com.karrar.movieapp.ui.profile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.LogoutUseCase
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {

    private val _editProfileUIEvent: MutableStateFlow<Event<EditProfileUIEvent?>> = MutableStateFlow(Event(null))
    val editProfileUIEvent= _editProfileUIEvent.asStateFlow()

    fun onCloseDialog() {
        _editProfileUIEvent.update { Event(EditProfileUIEvent.CloseDialogEvent) }
    }

    fun onClickGoToWebSite() {
        _editProfileUIEvent.update { Event(EditProfileUIEvent.OnGoToWebSite) }
    }
}