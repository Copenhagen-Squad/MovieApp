package com.karrar.movieapp.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.karrar.movieapp.databinding.SwitchToggleBinding


class CustomSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding =
        SwitchToggleBinding.inflate(LayoutInflater.from(context), this)

    private var isOn = false
    private var listener: ((Boolean) -> Unit)? = null

    init {
        updateUI()

        binding.switchContainer.setOnClickListener {
            toggle()
        }
    }

    private fun updateUI() {
        if (isOn) {
            binding.switchEnabledOn.visibility = VISIBLE
            binding.switchEnabledOff.visibility = GONE
        } else {
            binding.switchEnabledOn.visibility = GONE
            binding.switchEnabledOff.visibility = VISIBLE
        }
    }

    fun toggle() {
        isOn = !isOn
        updateUI()
        listener?.invoke(isOn)
    }

    fun setChecked(value: Boolean) {
        isOn = value
        updateUI()
    }

    fun isChecked(): Boolean = isOn

    fun setOnCheckedChangeListener(block: (Boolean) -> Unit) {
        listener = block
    }
}