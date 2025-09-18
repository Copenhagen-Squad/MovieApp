package com.karrar.movieapp.ui.components.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.AppbarBinding

class AppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding =
        AppbarBinding.inflate(LayoutInflater.from(context), this, true)

    private var onBackClick: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AppBar,
            0, 0
        ).apply {
            try {
                val title = getString(R.styleable.AppBar_AppBarTitle)
                val caption = getString(R.styleable.AppBar_AppBarCaption)
                val startIcon = getResourceId(R.styleable.AppBar_icon, 0)
                val showEndIcon = getBoolean(R.styleable.AppBar_showEndIcon, false)
                val showAppIcon = getBoolean(R.styleable.AppBar_showAppIcon, false)
                val showBack = getBoolean(R.styleable.AppBar_showIcon, true)


                setTitle(title ?: "")
                setCaption(caption)
                if (startIcon != 0) {
                    setEndIcon(startIcon)
                }
                binding.icBack.isVisible = showBack
                binding.icEndAction.isVisible = showEndIcon
                binding.icPlanet.isVisible = showAppIcon

            } finally {
                recycle()
            }
        }

        binding.icBack.setOnClickListener {
            if (onBackClick != null) {
                onBackClick?.invoke()
            } else {
                if (context is androidx.fragment.app.FragmentActivity) {
                    val activity = context as androidx.fragment.app.FragmentActivity
                    val navController = try {
                        androidx.navigation.Navigation.findNavController(activity, R.id.nav_host_fragment)
                    } catch (e: Exception) {
                        null
                    }
                    if (navController?.popBackStack() == false) {
                        activity.onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
        binding.icEndAction.setOnClickListener {
            // Default no-op
        }
    }

    fun setTitle(title: String) {
        binding.titleText.text = title
    }

    fun setCaption(caption: String?) {
        binding.captionText.isVisible = !caption.isNullOrEmpty()
        binding.captionText.text = caption
    }

    fun setOnBackClickListener(listener: () -> Unit) {
        onBackClick = listener
    }

    fun setEndIcon(resId: Int, onClick: (() -> Unit)? = null) {
        binding.icEndAction.setImageResource(resId)
        binding.icEndAction.setOnClickListener { onClick?.invoke() }
    }
}
