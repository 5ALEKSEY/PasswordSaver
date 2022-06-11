package com.ak.app_theme.theme.selectors

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.ak.app_theme.theme.CustomTheme

class CustomThemeRippleSelectorBuilder(private var theme: CustomTheme?) : CustomTheme.Support {

    private var state = SelectorStateData()
    private var addMask = false

    fun setColor(@ColorInt color: Int?) = apply { state.colorValue = color }
    fun setColorAttr(@AttrRes attr: Int?) = apply { state.colorAttribute = attr }
    fun addMask() = apply { addMask = true }

    fun build(): Drawable? {
        if (state.isNotEmpty()) {
            return buildRippleDrawable()
        }

        return null
    }

    private fun buildRippleDrawable(): Drawable {
        val builder = ColorStateListBuilder(theme)
        val mask = if (addMask) ColorDrawable(Color.WHITE) else null

        builder.addState(intArrayOf(), state)

        return RippleDrawable(builder.build(), null, mask)
    }

    fun getThemeId() = theme?.id ?: -1

    override fun applyTheme(theme: CustomTheme) {
        this.theme = theme
    }
}