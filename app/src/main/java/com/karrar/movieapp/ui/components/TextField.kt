package com.karrar.movieapp.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.widget.addTextChangedListener
import com.karrar.movieapp.R

class TextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val editText: EditText
    private val startIcon: ImageView

    private var onTextChanged: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.text_field, this, true)

        editText = findViewById(R.id.editText)
        startIcon = findViewById(R.id.start_icon)

        orientation = HORIZONTAL

        editText.addTextChangedListener {
            onTextChanged?.invoke(it.toString())
        }
        editText.setOnFocusChangeListener { _, hasFocus ->
            this.isSelected = hasFocus
        }
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setLeadingIcon(@DrawableRes resId: Int) {
        startIcon.setImageResource(resId)
        startIcon.visibility = View.VISIBLE
    }

    fun getText(): String = editText.text.toString()

    fun setText(text: String) {
        editText.setText(text)
    }

    fun setOnTextChangedListener(listener: (String) -> Unit) {
        onTextChanged = listener
    }
}
