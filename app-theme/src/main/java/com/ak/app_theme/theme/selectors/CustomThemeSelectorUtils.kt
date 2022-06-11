package com.ak.app_theme.theme.selectors

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.AttrRes
import androidx.core.graphics.drawable.DrawableCompat
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager

object CustomThemeSelectorUtils {

    private var cachedDrawables = SparseArray<Drawable?>()

    @JvmStatic
    fun clearDrawableCache() {
        cachedDrawables.clear()
    }

    private fun getCachedDrawable(@AttrRes styleAttr:Int): Drawable? {
        val drawable = cachedDrawables.get(styleAttr, null)
        drawable?.let {
            return it.constantState?.newDrawable() // clone
        }

        return null
    }

    private fun addCachedDrawable(@AttrRes styleAttr:Int, drawable: Drawable) {
        cachedDrawables.put(styleAttr, DrawableCompat.wrap(drawable).mutate())
    }

    @JvmStatic
    @JvmOverloads
    fun getColorBackgroundSelector(context: Context, attrs: AttributeSet?,
                                   isInEditMode: Boolean = false) : CustomThemeColorSelectorBuilder? {
        val theme = if (isInEditMode) null else CustomThemeManager.getCurrentTheme()
        return CustomThemeSelectorFabric.getColorBackgroundSelector(context, attrs, theme)
    }

    @JvmStatic
    fun getColorBackgroundSelector(theme: CustomTheme, @AttrRes styleAttr:Int) : CustomThemeColorSelectorBuilder? {
        return CustomThemeSelectorFabric.getColorBackgroundSelector(theme, styleAttr)
    }

    @JvmStatic
    @JvmOverloads
    fun getBackgroundSelector(context: Context, attrs: AttributeSet?,
                              isInEditMode: Boolean = false) : CustomThemeDrawableSelectorBuilder? {
        val theme = if (isInEditMode) null else CustomThemeManager.getCurrentTheme()
        return CustomThemeSelectorFabric.getBackgroundSelector(context, attrs, theme)
    }

    @JvmStatic
    fun getBackgroundSelector(theme: CustomTheme, @AttrRes styleAttr:Int) : CustomThemeDrawableSelectorBuilder? {
        return CustomThemeSelectorFabric.getBackgroundSelector(theme, styleAttr)
    }

    @JvmStatic
    fun getBackgroundSelector(@AttrRes styleAttr:Int) : CustomThemeDrawableSelectorBuilder? {
        return getBackgroundSelector(CustomThemeManager.getCurrentTheme(), styleAttr)
    }

    @JvmStatic
    fun getBackgroundDrawable(theme: CustomTheme, @AttrRes styleAttr:Int) : Drawable? {
        return getCachedDrawable(styleAttr) ?: getBackgroundSelector(theme, styleAttr)?.build()
            ?.apply { addCachedDrawable(styleAttr, this) }
    }

    @JvmStatic
    @JvmOverloads
    fun getImageSelector(context: Context, attrs: AttributeSet?,
                         isInEditMode: Boolean = false) : CustomThemeDrawableSelectorBuilder? {
        val theme = if (isInEditMode) null else CustomThemeManager.getCurrentTheme()
        return CustomThemeSelectorFabric.getImageSelector(context, attrs, theme)
    }

    @JvmStatic
    fun getImageSelector(theme: CustomTheme, @AttrRes styleAttr:Int) : CustomThemeDrawableSelectorBuilder? {
        return CustomThemeSelectorFabric.getImageSelector(theme, styleAttr)
    }

    @JvmStatic
    fun getImageSelector(@AttrRes styleAttr:Int) : CustomThemeDrawableSelectorBuilder? {
        return getImageSelector(CustomThemeManager.getCurrentTheme(), styleAttr)
    }

    @JvmStatic
    fun getImageDrawable(theme: CustomTheme, @AttrRes styleAttr:Int) : Drawable? {
        return getCachedDrawable(styleAttr) ?: getImageSelector(theme, styleAttr)?.build()
            ?.apply { addCachedDrawable(styleAttr, this) }
    }

    @JvmStatic
    fun getTextSelector(theme: CustomTheme, @AttrRes styleAttr:Int) : CustomThemeColorSelectorBuilder? {
        return CustomThemeSelectorFabric.getTextSelector(theme, styleAttr)
    }

    fun applyBackgroundTheme(theme: CustomTheme, selector: CustomThemeColorSelectorBuilder?) : Boolean {
        selector?.let {
            if (it.getThemeId() != theme.id) {
                if (it.hasStyle()) {
                    CustomThemeSelectorFabric.applyBackgroundStyle(theme, it.getStyle()!!, it)
                }
                it.applyTheme(theme)
                return true
            }
        }

        return false
    }

    fun applyBackgroundTheme(theme: CustomTheme, selector: CustomThemeDrawableSelectorBuilder?) : Boolean {
        selector?.let {
            if (it.getThemeId() != theme.id) {
                if (it.hasStyle()) {
                    CustomThemeSelectorFabric.applyBackgroundStyle(theme, it.getStyle()!!, it)
                }
                it.applyTheme(theme)
                return true
            }
        }

        return false
    }

    fun applyImageTheme(theme: CustomTheme, selector: CustomThemeDrawableSelectorBuilder?) : Boolean {
        selector?.let {
            if (it.getThemeId() != theme.id) {
                if (it.hasStyle()) {
                    CustomThemeSelectorFabric.applyImageStyle(theme, it.getStyle()!!, it)
                }
                it.applyTheme(theme)
                return true
            }
        }

        return false
    }

    fun applyTextTheme(theme: CustomTheme, selector: CustomThemeColorSelectorBuilder?) : Boolean {
        selector?.let {
            if (it.getThemeId() != theme.id) {
                if (it.hasStyle()) {
                    CustomThemeSelectorFabric.applyTextStyle(theme, it.getStyle()!!, it)
                }
                it.applyTheme(theme)
                return true
            }
        }

        return false
    }

    @JvmStatic
    fun setImageDrawable(builder: CustomThemeDrawableSelectorBuilder?, drawable: Drawable?) : Boolean {
        builder?.let {
            it.setDefaultDrawable(null as Int?)
            it.setDefaultDrawableAttr(null)
            it.setDefaultDrawable(drawable)
            return true
        }

        return false
    }

    @JvmStatic
    fun setImageResource(builder: CustomThemeDrawableSelectorBuilder?, resId: Int) : Boolean {
        builder?.let {
            it.setDefaultDrawable(null as Drawable?)
            it.setDefaultDrawableAttr(null)
            it.setDefaultDrawable(resId)
            return true
        }

        return false
    }

    @JvmStatic
    fun setAlpha(builder: CustomThemeDrawableSelectorBuilder?, alpha: Float) : Boolean {
        builder?.let {
            it.setAlpha(alpha)
            return true
        }

        return false
    }

    @JvmStatic
    fun setAlpha(builder: CustomThemeColorSelectorBuilder?, alpha: Float) : Boolean {
        builder?.let {
            it.setAlpha(alpha)
            return true
        }

        return false
    }

    @JvmStatic
    fun setTint(builder: CustomThemeDrawableSelectorBuilder?, colorAttr: Int) : Boolean {
        builder?.let {
            if (it.hasDefaultDrawable()) {
                it.setDefaultDrawableTintColorAttr(colorAttr)
            } else {
                it.setDefaultColorAttr(colorAttr)
            }
            return true
        }

        return false
    }

    @JvmStatic
    fun setTint(builder: CustomThemeColorSelectorBuilder?, colorAttr: Int) : Boolean {
        builder?.let {
            it.setDefaultColorAttr(colorAttr)
            return true
        }

        return false
    }

    @JvmStatic
    fun setTextColor(builder: CustomThemeColorSelectorBuilder?, color: Int) : Boolean {
        builder?.let {
            builder.setDefaultColorAttr(null)
            builder.setDefaultColor(color)
            return true
        }

        return false
    }

    @JvmStatic
    fun setTextColorAttr(builder: CustomThemeColorSelectorBuilder?, colorAttr: Int) : Boolean {
        builder?.let {
            builder.setDefaultColorAttr(colorAttr)
            builder.setDefaultColor(null)
            return true
        }

        return false
    }
}