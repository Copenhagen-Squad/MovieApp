package com.karrar.movieapp.ui.match.questions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Choice(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int = 0,
    @DrawableRes val icon: Int? = null,
    val isSelected: Boolean = false,
)
