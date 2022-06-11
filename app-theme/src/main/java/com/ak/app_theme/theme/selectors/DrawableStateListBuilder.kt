package com.ak.app_theme.theme.selectors

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import com.ak.app_theme.theme.CustomTheme

internal class DrawableStateListBuilder(private val theme: CustomTheme?) {
    private val states = mutableListOf<IntArray>()
    private val drawables = mutableListOf<Drawable>()

    fun addState(intArray: IntArray, drawable: Drawable) = apply {
        states.add(intArray)
        drawables.add(drawable)
    }

    fun addState(intArray: IntArray, color: Int) = addState(intArray, ColorDrawable(color))

    fun addState(intArray: IntArray, data: SelectorStateData) = apply {
        if (data.isNotEmpty()) {
            val alpha = data.alpha ?: 1f

            if (theme != null && data.colorAttribute != null) {
                data.colorAttribute?.let {
                    if (theme.isColor(it)) {
                        addState(intArray, theme.getColor(it, alpha))
                    }
                }
            } else if (data.colorValue != null) {
                data.colorValue?.let {
                    addState(intArray, CustomTheme.applyAlphaToColor(it, alpha))
                }
            }
        }
    }

    fun build() = StateListDrawable().apply {
        if (states.size == drawables.size) {
            states.forEachIndexed { index, state ->
                addState(state, drawables[index])
            }
        }
    }
}