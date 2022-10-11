package com.ak.app_theme.theme.applier

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemedView
import com.ak.app_theme.theme.selectors.CustomThemeColorSelectorBuilder
import com.ak.app_theme.theme.uicomponents.CustomThemeBackgroundSupport
import kotlin.math.max
import kotlin.math.min

object CustomThemeApplier {

    private const val TAG = "CustomThemeApplier"

    private fun findView(activity: Activity, themedView: CustomThemedView): View? {
        if (themedView.viewId > 0 && themedView.context != null) {
            return activity.findViewById(themedView.viewId)
        }

        return null
    }

    @JvmStatic
    fun setDrawableColor(drawable: Drawable, color: Int) {
        when (drawable) {
            is ShapeDrawable -> drawable.paint.color = color
            is ColorDrawable -> drawable.color = color
            else -> drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun safeMutate(drawable: Drawable): Drawable? {
        return try {
            drawable.mutate()
        } catch (e: Exception) {
            //LogUtil.error(TAG, "safeMutate", e)
            null
        }
    }

    fun tint(drawable: Drawable, color: Int): Drawable? = safeMutate(drawable)?.apply {
        setDrawableColor(this, color)
    }

    fun tint(theme: CustomTheme, drawable: Drawable, @AttrRes colorAttr: Int): Drawable? {
        return tint(drawable, theme.getColor(colorAttr))
    }

    fun getDrawable(context: Context, @DrawableRes drawableResId: Int): Drawable? {
        return try {
            AppCompatResources.getDrawable(context, drawableResId)
        } catch (e: Exception) {
            Log.e(TAG, "getDrawable", e)
            null
        }
    }

    fun getDrawable(theme: CustomTheme, @DrawableRes drawableResId: Int): Drawable? {
        return getDrawable(theme.context, drawableResId)
    }

    @JvmStatic
    fun getTintedDrawable(context: Context, @DrawableRes drawableResId: Int, @ColorInt color: Int): Drawable? {
        getDrawable(context, drawableResId)?.let {
            return tint(it, color)
        }

        return null
    }

    @JvmStatic
    fun getTintedDrawable(theme: CustomTheme, @DrawableRes drawableResId: Int, @AttrRes colorAttr: Int): Drawable? {
        getDrawable(theme, drawableResId)?.let {
            return tint(it, theme.getColor(colorAttr))
        }

        return null
    }

    @JvmStatic
    fun applyForeground(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view == null) {
            Log.d(TAG, "applyForeground for null view")
            return false
        }

        when {
            theme.isColor(resource) -> view.foreground = ColorDrawable(theme.getColor(resource))
            else -> {
                Log.d(TAG, "applyForeground - incorrect resource type: ${theme.getResourceName(resource)}")
                return false
            }
        }

        return true
    }

    @JvmStatic
    fun applyForegroundTint(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view == null) {
            Log.d(TAG, "applyForeground for null view")
            return false
        }

        if (theme.isColor(resource)) {
            view.foreground?.let {
                view.foreground = tint(it, theme.getColor(resource))
            }
        }

        return true
    }

    @JvmStatic
    fun applyBackground(theme: CustomTheme, resource: Int, vararg views: View?) {
        for (view in views) {
            applyBackground(theme, view, resource)
        }
    }

    @JvmStatic
    fun applyBackground(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view == null) {
            Log.d(TAG, "applyBackground for null view")
            return false
        }

        when {
            theme.isColor(resource) -> view.setBackgroundColor(theme.getColor(resource))
            else -> {
                Log.d(
                    TAG,
                    "applyBackground - incorrect resource type: ${theme.getResourceName(resource)}"
                )
                return false
            }
        }

        return true
    }

    @JvmStatic
    fun applyWindowBackground(theme: CustomTheme, window: Any?, resource: Int): Boolean {
        if (window == null) {
            Log.d(TAG, "applyWindowBackground for null window")
            return false
        }

        when (window) {
            is Window -> {
                if (theme.isColor(resource)) {
                    window.setBackgroundDrawable(ColorDrawable(theme.getColor(resource)))
                }
                return true
            }
            is PopupWindow -> {
                if (theme.isColor(resource)) {
                    window.setBackgroundDrawable(ColorDrawable(theme.getColor(resource)))
                }
                return true
            }
            else -> return false
        }
    }

    @JvmStatic
    fun applyBackgroundTint(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view == null) {
            Log.d(TAG, "applyBackgroundTint for null view")
            return false
        }

        if (ComplexViewsApplier.canApplyBackgroundTint(view)) {
            Log.d(TAG, "applyBackgroundTint. Complex view applier will handle it. view=$view")
            return false
        }

        if (theme.isColor(resource)) {
            if (view is CustomThemeBackgroundSupport) {
                view.setBackgroundTint(resource)
            } else {
                view.background?.let {
                    view.background = tint(it, theme.getColor(resource))
                }
            }

            return true
        }

        return false
    }

    @JvmStatic
    fun applyBackgroundAlpha(view: View?, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Boolean {
        if (view == null) {
            Log.d(TAG, "applyBackgroundAlpha for null view")
            return false
        }

        if (alpha !in 0.0..1.0) {
            Log.d(TAG, "applyBackgroundAlpha incorrect alpha value: $alpha")
            return false
        }

        if (view is CustomThemeBackgroundSupport) {
            view.setBackgroundAlpha(alpha)
        } else {
            view.background?.let {
                view.background = safeMutate(it)?.apply {
                    this.alpha = convertAlphaToDrawableRange(alpha)
                }
            }
        }

        return true
    }

    private fun convertAlphaToDrawableRange(
        @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ): Int { // 0..1 -> 0..255
        return (alpha * 255).toInt()
    }

    @JvmStatic
    fun applyCompoundDrawablesTint(theme: CustomTheme, attr: Int, vararg views: View?) {
        for (view in views) {
            applyCompoundDrawablesTint(theme, view, attr)
        }
    }

    @JvmStatic
    fun applyCompoundDrawablesTint(theme: CustomTheme, view: View?, attr: Int): Boolean {
        if (view !is TextView) {
            Log.d(TAG, "applyCompoundDrawablesTint for incorrect view: $view")
            return false
        }

        if (view.compoundDrawables.isEmpty()) {
            Log.d(TAG, "applyCompoundDrawablesTint. no compound drawables")
            return false
        }

        TextViewCompat.setCompoundDrawableTintList(view, ColorStateList.valueOf(theme.getColor(attr)))

        return true
    }

    @JvmStatic
    fun applyTextColor(theme: CustomTheme, resource: Int, vararg views: View?) {
        for (view in views) {
            applyTextColor(theme, view, resource)
        }
    }

    @JvmStatic
    fun applyTextColor(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view !is TextView) {
            Log.d(TAG, "applyTextColor for incorrect view: $view")
            return false
        }

        view.setTextColor(theme.getColor(resource))
        return true
    }

    @JvmStatic
    fun applyTextColor(
        theme: CustomTheme,
        view: View?,
        resource: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): Boolean {
        if (view !is TextView) {
            Log.d(TAG, "applyTextColor for incorrect view: $view")
            return false
        }

        if (alpha !in 0.0..1.0) {
            Log.d(TAG, "applyTextColor incorrect alpha: $alpha")
            return false
        }

        view.setTextColor(theme.getColor(resource, alpha))
        return true
    }

    @JvmStatic
    fun applyHintTextColor(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view !is EditText) {
            Log.d(TAG, "applyHintTextColor for incorrect view: $view")
            return false
        }

        view.setHintTextColor(theme.getColor(resource))
        return true
    }

    @JvmStatic
    fun applyImageSrc(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view !is ImageView) {
            Log.d(TAG, "applyImageSrc for incorrect view: $view")
            return false
        }

        if (theme.isColor(resource)) {
            val color = theme.getColor(resource)
            val drawable = view.drawable
            if (drawable != null) {
                view.setImageDrawable(tint(drawable, color))
            } else {
                view.setImageDrawable(ColorDrawable(color))
            }
        }

        return true
    }

    @JvmStatic
    fun applyImageTintSrc(
        theme: CustomTheme,
        view: View?,
        @DrawableRes drawableResId: Int,
        tintColorAttr: Int
    ): Boolean {
        if (view !is ImageView) {
            Log.d(TAG, "applyImageTintSrc for incorrect view: $view")
            return false
        }

        if (theme.isColor(tintColorAttr)) {
            getTintedDrawable(theme, drawableResId, tintColorAttr)?.let {
                view.setImageDrawable(it)
            }
        }

        return true
    }

    @JvmStatic
    fun applyTint(theme: CustomTheme, resource: Int, vararg views: View?) {
        for (view in views) {
            applyTint(theme, view, resource)
        }
    }

    @JvmStatic
    fun applyTint(theme: CustomTheme, view: View?, resource: Int?): Boolean {
        if (view !is ImageView) {
            Log.d(TAG, "applyTint for incorrect view: $view")
            return false
        }

        return when (resource != null) {
            true -> applyTint(theme, view, resource)
            false -> clearTint(view)
        }
    }

    private fun applyTint(theme: CustomTheme, view: View, resource: Int) =
        if (theme.isColor(resource)) {
            when (view) {
                is ImageView -> {
                    ImageViewCompat.setImageTintList(
                        view,
                        ColorStateList.valueOf(theme.getColor(resource))
                    )
                    true
                }
                else -> false
            }
        } else {
            false
        }

    private fun clearTint(view: View) = when (view) {
        is ImageView -> {
            ImageViewCompat.setImageTintList(view, null)
            true
        }
        else -> false
    }

    @SuppressLint("RestrictedApi")
    @JvmStatic
    fun applyButtonTint(theme: CustomTheme, view: View?, resource: Int): Boolean {
        if (view !is AppCompatCheckBox) {
            Log.d(TAG, "applyButtonTint for incorrect view: $view")
            return false
        }

        if (theme.isColor(resource)) {
            val color = theme.getColor(resource)
            view.supportButtonTintList = ColorStateList.valueOf(color)
        }

        return true
    }

    @JvmStatic
    fun applyForSwitch(
        theme: CustomTheme,
        view: SwitchCompat?,
        @AttrRes uncheckedThumbAttr: Int,
        @AttrRes checkedThumbAttr: Int,
        @AttrRes uncheckedTrackAttr: Int,
        @AttrRes checkedTrackAttr: Int,
    ): Boolean {
        if (view == null) {
            Log.d(TAG, "applyForSwitch for null view")
            return false
        }

        view.thumbTintList = CustomThemeColorSelectorBuilder(theme)
            .setDefaultColorAttr(uncheckedThumbAttr)
            .setUnCheckedColorAttr(uncheckedThumbAttr)
            .setCheckedColorAttr(checkedThumbAttr)
            .build()

        view.trackTintList = CustomThemeColorSelectorBuilder(theme)
            .setDefaultColorAttr(uncheckedTrackAttr)
            .setUnCheckedColorAttr(uncheckedTrackAttr)
            .setCheckedColorAttr(checkedTrackAttr)
            .build()

        return true
    }

    @JvmStatic
    fun applyForWindow(
        theme: CustomTheme,
        window: Window?,
        @AttrRes statusBarRes: Int,
        @AttrRes navigationBarRes: Int,
        isAppearanceLightNavigationBars: Boolean,
    ): Boolean {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = theme.getColor(statusBarRes)
            navigationBarColor = theme.getColor(navigationBarRes)
            WindowInsetsControllerCompat(
                this,
                decorView
            ).isAppearanceLightNavigationBars = isAppearanceLightNavigationBars
        }
        return true
    }

    @SuppressLint("NewApi")
    @JvmStatic
    fun applyTheme(activity: Activity, themedView: CustomThemedView, theme: CustomTheme): Boolean {
        val view = when (themedView.hasView()) {
            true -> themedView.getView()
            false -> findView(activity, themedView)
        } ?: return false

        themedView.getBackgroundResource()?.let {
            applyBackground(theme, view, it)
        }

        themedView.getBackgroundTintResource()?.let {
            applyBackgroundTint(theme, view, it)
        }

        themedView.getBackgroundAlpha()?.let {
            applyBackgroundAlpha(view, it)
        }

        themedView.getForegroundResource()?.let {
            applyForeground(theme, view, it)
        }

        themedView.getForegroundTintResource()?.let {
            applyForegroundTint(theme, view, it)
        }

        themedView.getTextColor()?.let {
            applyTextColor(theme, view, it)
        }

        themedView.getSrcResource()?.let {
            applyImageSrc(theme, view, it)
        }

        themedView.getTintResource()?.let {
            applyTint(theme, view, it)
        }

        themedView.getHintTextColor()?.let {
            applyHintTextColor(theme, view, it)
        }

        themedView.getButtonTintResource()?.let {
            applyButtonTint(theme, view, it)
        }

        ComplexViewsApplier.applyForComplexView(themedView, view, theme)

        return true
    }

    @JvmStatic
    fun getColor(context: Context, @AttrRes resource: Int): Int {
        return getColor(context, resource, 1f)
    }

    @JvmStatic
    fun getColor(
        context: Context,
        @AttrRes resource: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): Int {
        val typedValue = TypedValue()
        if (!context.theme.resolveAttribute(resource, typedValue, true)) {
            throw IllegalStateException("color with attr id=$resource not found")
        }

        val type = typedValue.type
        if (type != TypedValue.TYPE_INT_COLOR_ARGB4 && type != TypedValue.TYPE_INT_COLOR_ARGB8 &&
            type != TypedValue.TYPE_INT_COLOR_RGB4 && type != TypedValue.TYPE_INT_COLOR_RGB8
        ) {
            throw IllegalStateException("incorrect type for attr id=$resource")
        }

        if (alpha !in 0.0..1.0) {
            Log.d(TAG, "getColor incorrect alpha value: $alpha")
            return typedValue.data
        } else {
            return CustomTheme.applyAlphaToColor(typedValue.data, alpha)
        }
    }

    @JvmStatic
    fun getAutoColor(color: Int): Int {
        val alpha = Color.alpha(color)

        if (alpha > 0) {
            var red = Color.red(color)
            var green = Color.green(color)
            var blue = Color.blue(color)

            val luminance = ColorUtils.calculateLuminance(color)
            val k = if (luminance < 0.5) 25 else -25

            red = max(min(red + k, 255), 0)
            green = max(min(green + k, 255), 0)
            blue = max(min(blue + k, 255), 0)

            return Color.argb(alpha, red, green, blue)
        }

        return color
    }
}