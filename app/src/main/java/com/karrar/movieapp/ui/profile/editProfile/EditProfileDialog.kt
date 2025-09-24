package com.karrar.movieapp.ui.profile.editProfile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogLogoutBinding
import com.karrar.movieapp.databinding.EditProfileDialogBinding
import com.karrar.movieapp.ui.base.BaseDialog
import com.karrar.movieapp.ui.profile.logout.LogoutUIEvent
import com.karrar.movieapp.ui.profile.logout.LogoutViewModel
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setWidthPercent
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class EditProfileDialog : BaseDialog<EditProfileDialogBinding>() {

    override val layoutIdFragment: Int = R.layout.edit_profile_dialog
    override val viewModel: EditProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(90)
        collectLast(viewModel.editProfileUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: EditProfileUIEvent) {
        when (event) {
            EditProfileUIEvent.CloseDialogEvent -> {
                dismiss()
            }
            EditProfileUIEvent.OnGoToWebSite -> {
                val url = "https://www.themoviedb.org/login"
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                startActivity(intent)
            }
        }
    }

}