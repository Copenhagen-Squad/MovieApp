package com.karrar.movieapp.ui.movieDetails.rating

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ItemRatingBinding
import com.karrar.movieapp.ui.base.BaseDialog
import com.karrar.movieapp.ui.movieDetails.MovieDetailsViewModel
import com.karrar.movieapp.utilities.setWidthPercent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingBottomSheetDialog : BaseDialog<ItemRatingBinding>(), RatingInteractionListener {
    override val layoutIdFragment: Int = R.layout.item_rating
    override val viewModel: MovieDetailsViewModel by activityViewModels()

    private val args: RatingBottomSheetDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(90)

    }

    override fun onRatingChanged(rating: Float) {
        viewModel.onChangeRating(rating)
    }

    override fun onSubmitRating() {
        dismiss()
    }

    override fun onCloseDialog() {
        dismiss()
    }
}

interface RatingInteractionListener {
    fun onRatingChanged(rating: Float)
    fun onSubmitRating()
    fun onCloseDialog()
}