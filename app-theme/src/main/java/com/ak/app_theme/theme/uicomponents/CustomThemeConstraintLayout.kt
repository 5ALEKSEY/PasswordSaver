package com.ak.app_theme.theme.uicomponents

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.selectors.CustomThemeDrawableSelectorBuilder
import com.ak.app_theme.theme.selectors.CustomThemeSelectorUtils

open class CustomThemeConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    CustomTheme.Support, CustomThemeBackgroundSupport {

    companion object {
        private const val TAG = "CTConstraintLayout"
    }

    private var bgSelector = CustomThemeSelectorUtils.getBackgroundSelector(context, attrs, isInEditMode)

    init {
        applyBackgroundSelector()
    }

    override fun applyTheme(theme: CustomTheme) {
        if (CustomThemeSelectorUtils.applyBackgroundTheme(theme, bgSelector)) {
            applyBackgroundSelector()
        }
    }

    fun setBackgroundSelector(selector: CustomThemeDrawableSelectorBuilder) {
        bgSelector = selector
        applyBackgroundSelector()
    }

    private fun applyBackgroundSelector() {
        bgSelector?.build()?.let { super.setBackground(it) }
    }

    override fun setBackgroundColor(color: Int) {
        bgSelector?.let {
            Log.d(TAG, "Setting a custom background is not supported.")
            return
        }

        super.setBackgroundColor(color)
    }

    override fun setBackground(background: Drawable?) {
        bgSelector?.let {
            Log.d(TAG, "Setting a custom background is not supported.")
            return
        }

        super.setBackground(background)
    }

    override fun setBackgroundAlpha(alpha: Float) : Boolean {
        if (CustomThemeSelectorUtils.setAlpha(bgSelector, alpha)) {
            applyBackgroundSelector()
            return true
        }

        return false
    }

    override fun setBackgroundTint(colorAttr: Int) : Boolean {
        if (CustomThemeSelectorUtils.setTint(bgSelector, colorAttr)) {
            applyBackgroundSelector()
            return true
        }

        return false
    }
}