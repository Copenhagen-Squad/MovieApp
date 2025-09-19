package com.karrar.movieapp.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.SelectLanguageChipBinding

class LanguageChip @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding =
        SelectLanguageChipBinding.inflate(LayoutInflater.from(context), this, true)

    private var onLanguageSelected: ((String) -> Unit)? = null

    init {
        orientation = HORIZONTAL

        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val currentLang = prefs.getString("app_lang", "en") ?: "en"
        if (currentLang == "ar") {
            setChipSelected("ar")
        }else {
            setChipSelected("en")
        }

        binding.englishChip.setOnClickListener {
            setChipSelected("en")
            onLanguageSelected?.invoke("en")
        }

        binding.arabicChip.setOnClickListener {
            setChipSelected("ar")
            onLanguageSelected?.invoke("ar")
        }
    }

    private fun setChipSelected(lang: String) {
        val selectedColor = ContextCompat.getColor(context, R.color.brand_primary)
        val unselectedColor = ContextCompat.getColor(context, R.color.shade_primary)

        binding.englishChip.isSelected = lang == "en"
        binding.textEnglish.setTextColor(if (lang == "en") selectedColor else unselectedColor)

        binding.arabicChip.isSelected = lang == "ar"
        binding.textArabic.setTextColor(if (lang == "ar") selectedColor else unselectedColor)
    }

    fun setOnLanguageSelectedListener(listener: (String) -> Unit) {
        this.onLanguageSelected = { lang ->

            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("app_lang", lang).apply()
            listener(lang)

        }
    }

    fun setCurrentLanguage(lang: String) {
        setChipSelected(lang)
    }
}