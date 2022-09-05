package com.ak.app_theme.theme

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

class CustomThemeDrawableBuilder @JvmOverloads constructor(
    private val theme: CustomTheme?,
    private val context: Context? = null
) {

    private var targetShape: Int = GradientDrawable.RECTANGLE
    private var solidColor = 0

    private var strokeWidth = 0
    private var strokeColor = 0

    private var topLeftRadius = 0f
    private var topRightRadius = 0f
    private var bottomLeftRadius = 0f
    private var bottomRightRadius = 0f

    private var width = 0
    private var height = 0

    private var drawableAlpha = 1.0f

    private var insetsRect = Rect().apply { setEmpty() }
    private val layerDrawables = mutableListOf<Pair<Drawable, Rect>>()

    fun rectangle() = apply { targetShape = GradientDrawable.RECTANGLE }
    fun oval() = apply { targetShape = GradientDrawable.OVAL }
    fun ring() = apply { targetShape = GradientDrawable.RING }
    fun line() = apply { targetShape = GradientDrawable.LINE }

    fun solidColor(@ColorInt color: Int) = apply { solidColor = color }
    fun solidColorAttr(@AttrRes colorAttr: Int) = solidColor(theme?.getColor(colorAttr) ?: solidColor)

    fun strokeWidth(value: Int) = apply { strokeWidth = value }
    fun strokeWidthDp(value: Float) = apply { strokeWidth = value.dpToPx(context) }

    fun strokeColor(@ColorInt color: Int) = apply { strokeColor = color }
    fun strokeColorAttr(@AttrRes colorAttr: Int) =
        strokeColor(theme?.getColor(colorAttr) ?: strokeColor)

    fun topLeftRadius(value: Float) = apply { topLeftRadius = value }
    fun topLeftRadiusDp(value: Float) = topLeftRadius(value.dpToPx(context).toFloat())

    fun topRightRadius(value: Float) = apply { topRightRadius = value }
    fun topRightRadiusDp(value: Float) = topRightRadius(value.dpToPx(context).toFloat())

    fun bottomLeftRadius(value: Float) = apply { bottomLeftRadius = value }
    fun bottomLeftRadiusDp(value: Float) = bottomLeftRadius(value.dpToPx(context).toFloat())

    fun bottomRightRadius(value: Float) = apply { bottomRightRadius = value }
    fun bottomRightRadiusDp(value: Float) = bottomRightRadius(value.dpToPx(context).toFloat())

    fun radius(value: Float) = apply {
        topLeftRadius(value)
        topRightRadius(value)
        bottomLeftRadius(value)
        bottomRightRadius(value)
    }

    fun radiusDp(value: Float) = apply {
        topLeftRadiusDp(value)
        topRightRadiusDp(value)
        bottomLeftRadiusDp(value)
        bottomRightRadiusDp(value)
    }

    fun size(widthValue: Int, heightValue: Int) = apply { width = widthValue; height = heightValue }
    fun sizeDp(widthDp: Float, heightDp: Float) =
        size(widthDp.dpToPx(context), heightDp.dpToPx(context))

    fun alpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { drawableAlpha = alpha }

    fun insets(left: Int, top: Int, right: Int, bottom: Int) =
        apply { insetsRect.set(left, top, right, bottom) }

    fun insetsDp(leftDp: Float, topDp: Float, rightDp: Float, bottomDp: Float) = insets(
        leftDp.dpToPx(context),
        topDp.dpToPx(context),
        rightDp.dpToPx(context),
        bottomDp.dpToPx(context),
    )

    private fun isNotEmpty(rect: Rect) = rect.left > 0 || rect.right > 0 || rect.top > 0 || rect.bottom > 0

    fun addDrawableWithInsetsDp(
        drawable: Drawable,
        leftDp: Float, topDp: Float, rightDp: Float, bottomDp: Float
    ) = apply {
        val rect = Rect(
            leftDp.dpToPx(context),
            topDp.dpToPx(context),
            rightDp.dpToPx(context),
            bottomDp.dpToPx(context),
        )
        layerDrawables.add(Pair(drawable, rect))
    }

    fun addDrawableWithInsetsDp(drawable: Drawable, valueDp: Float) = addDrawableWithInsetsDp(
        drawable,
        valueDp,
        valueDp,
        valueDp,
        valueDp,
    )

    fun addDrawable(drawable: Drawable) = addDrawableWithInsetsDp(drawable, 0f)

    fun build(): Drawable {
        var drawable: Drawable = GradientDrawable().apply {
            shape = targetShape
            setColor(solidColor)
            if (strokeWidth > 0) {
                setStroke(strokeWidth, strokeColor)
            }
            if (width > 0 && height > 0) {
                setSize(width, height)
            }
            alpha = (drawableAlpha * 255f).toInt()
            cornerRadii = floatArrayOf(
                topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
            )
        }

        if (isNotEmpty(insetsRect) || layerDrawables.isNotEmpty()) {
            layerDrawables.add(0, Pair(drawable, insetsRect)) // add yourself at first pos

            val layerDrawable = LayerDrawable(layerDrawables.map { it.first }.toTypedArray())

            layerDrawables.mapIndexed { index, pair ->
                val rect = pair.second
                layerDrawable.setLayerInset(index, rect.left, rect.top, rect.right, rect.bottom)
            }

            drawable = layerDrawable
        }

        return drawable
    }
}