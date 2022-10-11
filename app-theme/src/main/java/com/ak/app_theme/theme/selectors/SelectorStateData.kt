package com.ak.app_theme.theme.selectors

import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class SelectorStateData(var drawable: Drawable? = null,
                             @DrawableRes var drawableResId: Int? = null,
                             @AttrRes var drawableAttribute: Int? = null,
                             @AttrRes var drawableTintColorAttribute: Int? = null,
                             @ColorInt var drawableTintColorValue: Int? = null,
                             var drawableTintConfigJson: String? = null,
                             @AttrRes var colorAttribute: Int? = null,
                             @ColorInt var colorValue: Int? = null,
                             var strokeWidthDp: Float? = null,
                             @AttrRes var strokeColorAttribute: Int? = null,
                             @ColorInt var strokeColorValue: Int? = null,
                             var widthDp: Float? = null,
                             var heightDp: Float? = null,
                             @FloatRange(from = 0.0, to = 1.0) var alpha: Float? = null) {

    private var radiusDp = RectF(0f, 0f, 0f, 0f)

    fun isNotEmpty() = hasDrawable() || colorAttribute != null || colorValue != null

    fun hasStroke() = strokeWidthDp != null && (strokeColorAttribute != null || strokeColorValue != null)

    fun hasTint() = drawableTintColorAttribute != null || drawableTintColorValue != null

    fun hasDrawable() = drawable != null || drawableResId != null || drawableAttribute != null

    fun hasSize() = widthDp != null && heightDp != null

    fun setRadiusDp(topLeft: Float, topRight: Float, bottomLeft: Float, bottomRight: Float) {
        radiusDp.set(topLeft, topRight, bottomLeft, bottomRight)
    }

    fun setTopLeftRadiusDp(dp: Float) = apply { radiusDp.left = dp }
    fun setTopRightRadiusDp(dp: Float) = apply { radiusDp.top = dp }
    fun setBottomLeftRadiusDp(dp: Float) = apply { radiusDp.right = dp }
    fun setBottomRightRadiusDp(dp: Float) = apply { radiusDp.bottom = dp }

    fun getTopLeftRadiusDp() = radiusDp.left
    fun getTopRightRadiusDp() = radiusDp.top
    fun getBottomLeftRadiusDp() = radiusDp.right
    fun getBottomRightRadiusDp() = radiusDp.bottom

    fun setRadiusDp(value: Float) = setRadiusDp(value, value, value, value)

    fun hasTintConfig() = drawableTintConfigJson?.isNotEmpty() ?: false

    fun getTintConfig(gson: Gson) : VectorDrawableTintConfig? {
        if(hasTintConfig()) {
            val type = object : TypeToken<List<VectorDrawablePathColorItem>>() {}.type
            val items = try {
                gson.fromJson<List<VectorDrawablePathColorItem>>(drawableTintConfigJson, type)
            } catch (e: Exception) {
                null
            }

            if (items?.isNotEmpty() == true) {
                return VectorDrawableTintConfig(items)
            }
        }

        return null
    }
}

data class VectorDrawablePathColorItem(val path:String, val colorAttr: String)
data class VectorDrawableTintConfig(val items: List<VectorDrawablePathColorItem>)