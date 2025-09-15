package com.karrar.movieapp.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SignUpBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the views in the bottom sheet layout
        val goWebsiteButton = view.findViewById<TextView>(R.id.button_go_to_website)
        val cancelButton = view.findViewById<TextView>(R.id.button_cancel)

        // Set up click listeners for the buttons
        goWebsiteButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.TMDB_SIGNUP_URL))
            startActivity(browserIntent)
            dismiss() // Close the bottom sheet after the action
        }

        cancelButton.setOnClickListener {
            dismiss() // Close the bottom sheet
        }
    }

    companion object {
        const val TAG = "SignUpBottomSheetFragment"
    }
}