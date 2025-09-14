package com.karrar.movieapp.domain.usecases.login

import com.karrar.movieapp.utilities.FormFieldState
import javax.inject.Inject

class ValidateFiledUseCase @Inject constructor(){
    operator fun invoke(text: String) : FormFieldState {
        if (!text.all { it.isLetterOrDigit()|| it == '_'  }) {
            return FormFieldState.InValid("Usernames can only include letters and numbers")
        }

        return FormFieldState.Valid
    }
}