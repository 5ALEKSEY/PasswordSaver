package com.ak.app_theme.theme.selectors

import android.content.res.ColorStateList
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.ak.app_theme.theme.CustomTheme

class CustomThemeColorSelectorBuilder(private var theme: CustomTheme?) : CustomTheme.Support {

    @AttrRes
    private var styleAttr: Int? = null

    private var defaultState = SelectorStateData()
    private var pressedState = SelectorStateData()
    private var selectedState = SelectorStateData()
    private var focusedState = SelectorStateData()
    private var unCheckedState = SelectorStateData()
    private var checkedState = SelectorStateData()
    private var disabledState = SelectorStateData()

    fun setStyle(@AttrRes attr: Int) = apply { styleAttr = attr }
    fun getStyle() = styleAttr
    fun hasStyle() = styleAttr != null

    fun setDefaultColor(@ColorInt color: Int?) = apply { defaultState.colorValue = color }
    fun setDefaultColorAttr(@AttrRes attr: Int?) = apply { defaultState.colorAttribute = attr }
    fun setDefaultColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { defaultState.alpha = alpha }

    fun setPressedColor(@ColorInt color: Int?) = apply { pressedState.colorValue = color }
    fun setPressedColorAttr(@AttrRes attr: Int?) = apply { pressedState.colorAttribute = attr }
    fun setPressedColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { pressedState.alpha = alpha }

    fun setSelectedColor(@ColorInt color: Int?) = apply { selectedState.colorValue = color }
    fun setSelectedColorAttr(@AttrRes attr: Int?) = apply { selectedState.colorAttribute = attr }
    fun setSelectedColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { selectedState.alpha = alpha }

    fun setFocusedColor(@ColorInt color: Int?) = apply { focusedState.colorValue = color }
    fun setFocusedColorAttr(@AttrRes attr: Int?) = apply { focusedState.colorAttribute = attr }
    fun setFocusedColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { focusedState.alpha = alpha }

    fun setUnCheckedColor(@ColorInt color: Int?) = apply { unCheckedState.colorValue = color }
    fun setUnCheckedColorAttr(@AttrRes attr: Int?) = apply { unCheckedState.colorAttribute = attr }
    fun setUnCheckedColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { unCheckedState.alpha = alpha }

    fun setCheckedColor(@ColorInt color: Int?) = apply { checkedState.colorValue = color }
    fun setCheckedColorAttr(@AttrRes attr: Int?) = apply { checkedState.colorAttribute = attr }
    fun setCheckedColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { checkedState.alpha = alpha }

    fun setDisabledColor(@ColorInt color: Int?) = apply { disabledState.colorValue = color }
    fun setDisabledColorAttr(@AttrRes attr: Int?) = apply { disabledState.colorAttribute = attr }
    fun setDisabledColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { disabledState.alpha = alpha }

    fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply {
        setDefaultColorAlpha(alpha)
        setPressedColorAlpha(alpha)
        setSelectedColorAlpha(alpha)
        setFocusedColorAlpha(alpha)
        setUnCheckedColorAlpha(alpha)
        setCheckedColorAlpha(alpha)
        setDisabledColorAlpha(alpha)
    }

    private fun getDefaultColorValue() : Int? {
        if (theme != null) {
            defaultState.colorAttribute?.let { colorAttr ->
                if (theme?.isColor(colorAttr) == true) {
                    return theme?.getColor(colorAttr)
                }
            }
        }

        return defaultState.colorValue
    }

    fun ready() = defaultState.isNotEmpty()

    fun build(): ColorStateList? {
        if (ready()) {
            val builder = ColorStateListBuilder(theme)
            val defaultColor = getDefaultColorValue()

            builder.addState(intArrayOf(android.R.attr.state_pressed), pressedState, defaultColor)
            builder.addState(intArrayOf(android.R.attr.state_selected), selectedState, defaultColor)
            builder.addState(intArrayOf(android.R.attr.state_focused), focusedState, defaultColor)
            builder.addState(intArrayOf(-android.R.attr.state_checked), unCheckedState, defaultColor)
            builder.addState(intArrayOf(android.R.attr.state_checked), checkedState, defaultColor)
            builder.addState(intArrayOf(-android.R.attr.state_enabled), disabledState, defaultColor)
            builder.addState(intArrayOf(), defaultState, null)

            return builder.build()
        }

        return null
    }

    fun getThemeId() = theme?.id ?: -1

    override fun applyTheme(theme: CustomTheme) {
        this.theme = theme
    }
}