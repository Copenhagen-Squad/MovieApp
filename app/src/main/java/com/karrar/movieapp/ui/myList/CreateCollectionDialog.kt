package com.karrar.movieapp.ui.myList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentCreateListDialogBinding
import com.karrar.movieapp.ui.base.BaseDialogFragment
import com.karrar.movieapp.ui.myList.myCollectionUIState.MyCollectionUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCollectionDialog : BaseDialogFragment<FragmentCreateListDialogBinding>() {

    override val layoutIdFragment = R.layout.fragment_create_list_dialog
    override val viewModel: MyCollectionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collectionNameField.setOnTextChangedListener { text ->
            viewModel.onListNameInputChange(text)
        }

        collectLast(viewModel.createListDialogUIState) { state ->
            binding.logoutButton.isEnabled = state.mediaListName.isNotBlank()
            binding.logoutButton.alpha = if (state.mediaListName.isNotBlank()) 1f else 0.5f
        }

        collectLast(viewModel.myListUIEvent) {
            it.peekContent()?.let { event ->
                if (event is MyCollectionUIEvent.CLickAddEvent) {
                    dismissDialog()
                }
            }
        }
    }

    private fun dismissDialog() {
        this.dismiss()
    }

}