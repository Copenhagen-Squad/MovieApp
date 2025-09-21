package com.karrar.movieapp.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.karrar.movieapp.R

class ContentPreferences @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var selectedOption: Int = -1
    private var onOptionSelected: ((Option) -> Unit)? = null

    private lateinit var titleHide: TextView
    private lateinit var titleFilter: TextView
    private lateinit var titleShow: TextView

    private lateinit var iconHide: ImageView
    private lateinit var iconFilter: ImageView
    private lateinit var iconShow: ImageView

    private lateinit var optionHide: View
    private lateinit var optionFilter: View
    private lateinit var optionShow: View

    enum class Option { HIDE, FILTER, SHOW }

    init {
        LayoutInflater.from(context).inflate(R.layout.select_content_preferences, this, true)

        titleHide = findViewById(R.id.title_content_hide)
        titleFilter = findViewById(R.id.title_content_filter)
        titleShow = findViewById(R.id.title_content_show)

        iconHide = findViewById(R.id.icon_hide)
        iconFilter = findViewById(R.id.icon_filter)
        iconShow = findViewById(R.id.icon_show)

        optionHide = findViewById(R.id.option_hide)
        optionFilter = findViewById(R.id.option_filter)
        optionShow = findViewById(R.id.option_show)

        optionHide.setOnClickListener { selectOption(Option.HIDE) }
        optionFilter.setOnClickListener { selectOption(Option.FILTER) }
        optionShow.setOnClickListener { selectOption(Option.SHOW) }
    }

    private fun selectOption(option: Option) {
        selectedOption = option.ordinal
        onOptionSelected?.invoke(option)

        resetColors()

        when (option) {
            Option.HIDE -> {
                optionHide.isSelected = true
                titleHide.setTextColor(ContextCompat.getColor(context, R.color.brand_primary))
                iconHide.imageTintList = ContextCompat.getColorStateList(context, R.color.brand_primary)
            }
            Option.FILTER -> {
                optionFilter.isSelected = true
                titleFilter.setTextColor(ContextCompat.getColor(context, R.color.brand_primary))
                iconFilter.imageTintList = ContextCompat.getColorStateList(context, R.color.brand_primary)
            }
            Option.SHOW -> {
                optionShow.isSelected = true
                titleShow.setTextColor(ContextCompat.getColor(context, R.color.brand_primary))
                iconShow.imageTintList = ContextCompat.getColorStateList(context, R.color.brand_primary)
            }
        }
    }

    private fun resetColors() {
        val defaultColor = ContextCompat.getColor(context, R.color.shade_primary)
        titleHide.setTextColor(defaultColor)
        titleFilter.setTextColor(defaultColor)
        titleShow.setTextColor(defaultColor)

        iconHide.imageTintList = ColorStateList.valueOf(defaultColor)
        iconFilter.imageTintList = ColorStateList.valueOf(defaultColor)
        iconShow.imageTintList = ColorStateList.valueOf(defaultColor)

        optionHide.isSelected = false
        optionFilter.isSelected = false
        optionShow.isSelected = false
    }

    fun setOnOptionSelectedListener(listener: (Option) -> Unit) {
        onOptionSelected = listener
    }

    fun getSelectedOption(): Option? =
        if (selectedOption == -1) null else Option.values()[selectedOption]
}
