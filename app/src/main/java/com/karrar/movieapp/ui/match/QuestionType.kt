package com.karrar.movieapp.ui.match

import androidx.annotation.StringRes
import com.karrar.movieapp.R
enum class QuestionType(@StringRes val titleResource: Int) {
    MOOD(R.string.what_mood),
    GENRE(R.string.pick_genre),
    TIME(R.string.how_much_time),
    TYPE(R.string.recent_or_classic)
}