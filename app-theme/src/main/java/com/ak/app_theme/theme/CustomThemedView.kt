package com.ak.app_theme.theme

import android.content.Context
import android.view.View
import androidx.annotation.FloatRange

class CustomThemedView private constructor(
    var context: Context?,
    val viewId: Int,
    private val viewClassName: String,
    private var view: View?,
    private val attributes: HashMap<String, Any>
) {

    companion object {
        val androidAttributes = arrayOf(
            "background",
            "backgroundTint",
            "foreground",
            "foregroundTint",
            "textColor",
            "tint",
            "src",
            "textColorHint",
            "thumbTint",
            "buttonTint",
        )

        val appAttributes = arrayOf(
            "srcCompat",
            "tint",
            "backgroundTint",
            "divider",
            "widgetPopupBackground",
            "cardBackgroundColor",
            "buttonTint",
        )

        val alphaAttributes = arrayOf(
            "srcAlpha",
            "backgroundAlpha",
            "textAlpha",
        )

        private val BACKGROUND_ATTR = androidAttributes[0]
        private val BACKGROUND_TINT_ATTR = androidAttributes[1]
        private val FOREGROUND_ATTR = androidAttributes[2]
        private val FOREGROUND_TINT_ATTR = androidAttributes[3]
        private val TEXT_COLOR_ATTR = androidAttributes[4]
        private val TINT_ATTR = androidAttributes[5]
        private val SRC_ATTR = androidAttributes[6]
        private val HINT_TEXT_COLOR_ATTR = androidAttributes[7]
        private val THUMB_TINT_ATTR = androidAttributes[8]
        private val BUTTON_TINT_ATTR = androidAttributes[9]

        private val SRC_COMPAT_ATTR = appAttributes[0]
        private val TINT_COMPAT_ATTR = appAttributes[1]
        private val BACKGROUND_TINT_COMPAT_ATTR = appAttributes[2]
        private val DIVIDER_ATTR = appAttributes[3]
        private val POPUP_BACKGROUND_ATTR = appAttributes[4]
        private val CARD_BACKGROUND_ATTR = appAttributes[5]
        private val BUTTON_TINT_COMPAT_ATTR = appAttributes[6]

        private val SRC_ALPHA_ATTR = alphaAttributes[0]
        private val BACKGROUND_ALPHA_ATTR = alphaAttributes[1]
        private val TEXT_ALPHA_ATTR = alphaAttributes[2]
    }

    fun hasView() = (view != null)
    fun getView() = view
    fun resetView() {
        view = null
    }

    private fun getIntValue(attr: String) = attributes[attr] as? Int
    private fun getFloatValue(attr: String) = attributes[attr] as? Float

    private fun getIntValueFrom(vararg attrs: String): Int? {
        for (attr in attrs) {
            if (attributes.containsKey(attr)) {
                return getIntValue(attr)
            }
        }

        return null
    }

    fun getBackgroundResource() = getIntValue(BACKGROUND_ATTR)
    fun getBackgroundTintResource() = getIntValueFrom(BACKGROUND_TINT_ATTR, BACKGROUND_TINT_COMPAT_ATTR)
    fun getForegroundResource() = getIntValue(FOREGROUND_ATTR)
    fun getForegroundTintResource() = getIntValue(FOREGROUND_TINT_ATTR)
    fun getTextColor() = getIntValue(TEXT_COLOR_ATTR)
    fun getHintTextColor() = getIntValue(HINT_TEXT_COLOR_ATTR)
    fun getDividerResource() = getIntValue(DIVIDER_ATTR)
    fun getPopupBackgroundResource() = getIntValue(POPUP_BACKGROUND_ATTR)
    fun getTintResource() = getIntValueFrom(TINT_ATTR, TINT_COMPAT_ATTR)
    fun getSrcResource() = getIntValueFrom(SRC_ATTR, SRC_COMPAT_ATTR)
    fun getThumbTintResource() = getIntValue(THUMB_TINT_ATTR)
    fun getSrcAlpha() = getFloatValue(SRC_ALPHA_ATTR)
    fun getBackgroundAlpha() = getFloatValue(BACKGROUND_ALPHA_ATTR)
    fun getTextAlpha() = getFloatValue(TEXT_ALPHA_ATTR)
    fun getCardBackgroundColor() = getIntValue(CARD_BACKGROUND_ATTR)
    fun getButtonTintResource() = getIntValueFrom(BUTTON_TINT_ATTR, BUTTON_TINT_COMPAT_ATTR)

    private fun getResourceName(resourceId: Int) = try {
        context?.resources?.getResourceName(resourceId) ?: "null"
    } catch (e: Exception) {
        "unknown res id=$resourceId"
    }

    fun destroy() {
        context = null
    }

    override fun toString(): String {
        return "id:${getResourceName(viewId)}, class: $viewClassName, attrs: $attributes"
    }

    data class Builder(private val context: Context) {
        private var viewId = -1
        private var viewClassName = ""
        private var view: View? = null
        private var attributes = HashMap<String, Any>()

        fun setViewId(value: Int) = apply { viewId = value }
        fun setViewClassName(value: String) = apply { viewClassName = value }
        fun setView(value: View?) = apply { view = value }
        fun addAttribute(name: String, value: Int) = apply { attributes[name] = value }
        fun addAttributes(value: HashMap<String, Any>) = apply { attributes.putAll(value) }
        fun setBackground(value: Int) = apply { attributes[BACKGROUND_ATTR] = value }
        fun setBackgroundTint(value: Int) = apply { attributes[BACKGROUND_TINT_ATTR] = value }
        fun setForeground(value: Int) = apply { attributes[FOREGROUND_ATTR] = value }
        fun setForegroundTint(value: Int) = apply { attributes[FOREGROUND_TINT_ATTR] = value }
        fun setTextColor(value: Int) = apply { attributes[TEXT_COLOR_ATTR] = value }
        fun setTintResource(value: Int) = apply { attributes[TINT_ATTR] = value }
        fun setSrc(value: Int) = apply { attributes[SRC_ATTR] = value }
        fun setDividerResource(value: Int) = apply { attributes[DIVIDER_ATTR] = value }
        fun setBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) value: Float) =
            apply { attributes[BACKGROUND_ALPHA_ATTR] = value }

        fun setSrcAlpha(@FloatRange(from = 0.0, to = 1.0) value: Float) =
            apply { attributes[SRC_ALPHA_ATTR] = value }

        fun setTextAlpha(@FloatRange(from = 0.0, to = 1.0) value: Float) =
            apply { attributes[TEXT_ALPHA_ATTR] = value }

        fun build() = CustomThemedView(context, viewId, viewClassName, view, attributes)
    }
}