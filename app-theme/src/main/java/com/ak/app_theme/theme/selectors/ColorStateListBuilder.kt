package com.ak.app_theme.theme.selectors

import android.content.res.ColorStateList
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeApplier

internal class ColorStateListBuilder(private val theme: CustomTheme?) {
    private val states = mutableListOf<IntArray>()
    private val colors = mutableListOf<Int>()

    fun addState(intArray: IntArray, color: Int) = apply {
        states.add(intArray)
        colors.add(color)
    }

    fun addState(intArray: IntArray, data: SelectorStateData, defaultColorValue:Int? = null) = apply {
        if (data.isNotEmpty()) {
            val alpha = data.alpha ?: 1f

            if (theme != null && data.colorAttribute != null) {
                data.colorAttribute?.let { colorAttr ->
                    when {
                        colorAttr == R.attr.auto && defaultColorValue != null -> {
                            addState(intArray, CustomThemeApplier.getAutoColor(defaultColorValue))
                        }
                        theme.isColor(colorAttr) -> addState(intArray, theme.getColor(colorAttr, alpha))
                        else -> {

                        }
                    }
                }
            } else if (data.colorValue != null) {
                data.colorValue?.let {
                    addState(intArray, CustomTheme.applyAlphaToColor(it, alpha))
                }
            }
        }
    }

    fun addDrawableTintState(intArray: IntArray, data: SelectorStateData) = apply {
        if (data.isNotEmpty()) {
            val alpha = data.alpha ?: 1f

            if (theme != null && data.drawableTintColorAttribute != null) {
                data.drawableTintColorAttribute?.let {
                    if (theme.isColor(it)) {
                        addState(intArray, theme.getColor(it, alpha))
                    }
                }
            } else if (data.drawableTintColorValue != null) {
                data.drawableTintColorValue?.let {
                    addState(intArray, CustomTheme.applyAlphaToColor(it, alpha))
                }
            }
        }
    }

    fun build() = ColorStateList(states.toTypedArray(), colors.toIntArray())
}