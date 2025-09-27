package com.karrar.movieapp.ui.movieDetails.rating

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ItemRatingBinding
import com.karrar.movieapp.ui.base.BaseDialog
import com.karrar.movieapp.ui.movieDetails.MovieDetailsUIEvent
import com.karrar.movieapp.ui.movieDetails.MovieDetailsViewModel
import com.karrar.movieapp.utilities.setWidthPercent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RatingBottomSheetDialog : BaseDialog<ItemRatingBinding>() {
    override val layoutIdFragment: Int = R.layout.item_rating
    override val viewModel: MovieDetailsViewModel by viewModels()
    private val args: RatingBottomSheetDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(90)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieDetailsUIEvent.collect { event ->
                event.getContentIfNotHandled()?.let { uiEvent ->
                    when (uiEvent) {
                        is MovieDetailsUIEvent.MessageAppear -> {
                            dismiss()
                        }
                        else -> { }
                    }
                }
            }
        }
    }
}