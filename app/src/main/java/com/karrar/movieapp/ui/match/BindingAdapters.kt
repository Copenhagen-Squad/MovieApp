package com.karrar.movieapp.ui.match

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import coil.load
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.category.uiState.ErrorUIState
import com.karrar.movieapp.utilities.ErrorUI

@BindingAdapter("imageUrl")
fun ImageView.bindImage(url: String?) {
    val iv = this
    iv.load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_movie)
        error(R.drawable.ic_movie)

        listener(
            onStart = {
                iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
                iv.setBackgroundResource(R.drawable.gradient)
            },
            onError = { _, _ ->
                iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
                iv.setBackgroundResource(R.drawable.gradient)
            },
            onSuccess = { _, _ ->
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                iv.background = null
            }
        )
    }
}

@BindingAdapter("textSafe")
fun TextView.bindTextSafe(v: String?) {
    text = v ?: ""
}

@BindingAdapter("textFormatted")
fun TextView.bindRating(v: Double?) {
    text = v?.let { String.format("%.1f", it) } ?: "--"
}

@BindingAdapter("app:showWhenNoInternet")
fun showWhenNoInternet(view: View, errors: List<ErrorUIState>?) {
    view.isVisible = errors?.any { it.code == ErrorUI.INTERNET_CONNECTION } == true
}

@BindingAdapter("app:showWhenNoLogin")
fun showWhenNoLogin(view: View, errors: List<ErrorUIState>?) {
    view.isVisible = errors?.any { it.code == ErrorUI.NEED_LOGIN } == true
}