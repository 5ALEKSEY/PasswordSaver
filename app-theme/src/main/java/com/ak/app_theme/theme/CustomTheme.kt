package com.ak.app_theme.theme

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.res.use
import androidx.core.util.containsKey
import androidx.core.util.isNotEmpty
import com.ak.app_theme.R

class CustomTheme private constructor(
    val context: Context,
    val id: Int,
    val nameResId: Int,
    @StyleRes val themeStyle: Int,
    val isLight: Boolean,
    val overriddenColors: SparseIntArray = SparseIntArray(),
) {

    val isNative = themeStyle == 0
    val isCustom = overriddenColors.isNotEmpty()

    companion object {
        private const val TAG = "CustomTheme"

        @JvmStatic
        fun applyAlphaToColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
            return Color.argb(
                (alpha * 255).toInt(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
        }

        @JvmStatic
        fun getColorAlpha(color: Int): Float = Color.alpha(color) / 255f
    }

    private val colors = SparseIntArray()

    init {
        if (!isNative) {
            context.obtainStyledAttributes(themeStyle, R.styleable.CustomThemeAttributes).use {
                val contextWrapper = ContextThemeWrapper(context, themeStyle)

                for (position in 0 until it.indexCount) {
                    val index = it.getIndex(position)
                    val attributeId = R.styleable.CustomThemeAttributes[index]

                    if (overriddenColors.containsKey(attributeId)) {
                        colors.put(attributeId, overriddenColors[attributeId])
                        continue
                    }

                    val resId = it.getResourceId(index, 0)
                    if (resId != 0) {
                        when (context.resources.getResourceTypeName(resId)) {
                            "color" -> colors.put(attributeId, it.getColor(index, 0))
                            else -> Log.e(
                                TAG,
                                "unsupported resource type: ${context.resources.getResourceTypeName(
                                    resId
                                )}, " +
                                    "for attribute: ${getResourceName(attributeId)} in theme: ${getThemeName()}"
                            )
                        }
                    } else {
                        val value = it.peekValue(index)

                        if (value != null && !parseTypedValue(attributeId, value)) {
                            if (value.type == TypedValue.TYPE_ATTRIBUTE) {
                                val attrValue = TypedValue()
                                var parsed = false
                                if (contextWrapper.theme.resolveAttribute(
                                        value.data,
                                        attrValue,
                                        true
                                    )
                                ) {
                                    parsed = parseTypedValue(attributeId, attrValue)
                                }

                                if (!parsed) {
                                    Log.e(
                                        TAG,
                                        "can not resolve value for attribute: ${getResourceName(
                                            attributeId
                                        )} in theme: ${getThemeName()}"
                                    )
                                }
                            } else {
                                Log.e(
                                    TAG, "unsupported value type: ${value.type}, " +
                                    "for attribute: ${getResourceName(attributeId)} in theme: ${getThemeName()}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }



    private fun getThemeName() = context.getString(nameResId)

    private fun parseTypedValue(attributeId: Int, value: TypedValue): Boolean {
        when (value.type) {
            TypedValue.TYPE_INT_COLOR_ARGB4,
            TypedValue.TYPE_INT_COLOR_ARGB8,
            TypedValue.TYPE_INT_COLOR_RGB4,
            TypedValue.TYPE_INT_COLOR_RGB8 -> {
                colors.put(attributeId, value.data)
            }
            else -> {
                return false
            }
        }

        return true
    }

    fun getResourceName(resourceId: Int): String {
        return try {
            context.resources.getResourceName(resourceId)
        } catch (e: Exception) {
            "unknown res id=$resourceId"
        }
    }

    fun exist(array: SparseIntArray, @AttrRes attribute: Int): Boolean {
        return array.get(attribute, 0) != 0 || array.get(attribute, 1) != 1
    }

    fun isColor(@AttrRes attribute: Int) = exist(colors, attribute)

    @Throws(Resources.NotFoundException::class)
    fun getColor(@AttrRes attribute: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        if (!isColor(attribute)) {
            throw Resources.NotFoundException("color ${getResourceName(attribute)} not found in theme: ${getThemeName()}")
        }

        val color = colors.get(attribute)

        if (alpha !in 0.0..1.0) {
            return color
        }

        return applyAlphaToColor(color, alpha)
    }

    @Throws(Resources.NotFoundException::class)
    fun getColor(@AttrRes attribute: Int): Int {
        if (!isColor(attribute)) {
            throw Resources.NotFoundException("color ${getResourceName(attribute)} not found in theme: ${getThemeName()}")
        }

        return colors.get(attribute)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomTheme

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    interface Support {
        fun applyTheme(theme: CustomTheme)
    }

    data class Description(
        val id: Int,
        val nameResId: Int,
        val isLight: Boolean,
        @ColorInt
        val impressColor1: Int,
        @ColorInt
        val impressColor2: Int,
        @ColorInt
        val impressColor3: Int,
    )

    data class Builder(private val context: Context) {
        private var themeId = -1
        private var themeNameResId = 0
        private var themeStyle = 0
        private var themeIsLight = true
        private val overriddenColors = SparseIntArray()

        fun id(id: Int) = apply { themeId = id }
        fun name(@StringRes nameRes: Int) = apply { themeNameResId = nameRes }
        fun themeStyle(@StyleRes style: Int) = apply { themeStyle = style }
        fun lightThemeFlag(isLight: Boolean) = apply { themeIsLight = isLight }
        fun overrideColorAttr(@AttrRes colorAttr: Int, @ColorInt newValue: Int) = apply {
            overriddenColors.put(colorAttr, newValue)
        }
        fun build() = CustomTheme(
            context = context,
            id = themeId,
            nameResId = themeNameResId,
            themeStyle = themeStyle,
            isLight = themeIsLight,
            overriddenColors = overriddenColors,
        )
    }
}

fun CustomTheme.toDescription() = CustomTheme.Description(
    id,
    nameResId,
    isLight,
    if (isNative) 0 else getColor(R.attr.themedPrimaryColor),
    if (isNative) 0 else getColor(R.attr.themedPrimaryDarkColor),
    if (isNative) 0 else getColor(R.attr.themedPrimaryLightColor),
)