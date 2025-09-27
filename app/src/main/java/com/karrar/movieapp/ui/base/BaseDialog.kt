package com.karrar.movieapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karrar.movieapp.BR

abstract class BaseDialog<VDB : ViewDataBinding> : BottomSheetDialogFragment(){
    abstract val layoutIdFragment: Int
    abstract val viewModel: ViewModel

    private lateinit var _binding: VDB
    protected val binding: VDB
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutIdFragment, container, false)
        _binding.apply {
            lifecycleOwner = viewLifecycleOwner
            // Try to set the viewModel variable if it exists
            try {
                setVariable(BR.viewModel, viewModel)
            } catch (e: Exception) {
                // Handle cases where the variable doesn't exist or types don't match
                // You could add logging here if needed
            }
            return root
        }
    }
}