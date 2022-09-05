package com.ak.app_theme.theme.selectors

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.Log
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeDrawableBuilder
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.dpToPx
import com.ak.app_theme.theme.drawable.VectorDrawableCompat
import com.google.gson.Gson

@Suppress("unused")
class CustomThemeDrawableSelectorBuilder @JvmOverloads constructor(
    private var theme: CustomTheme?,
    private val context: Context? = null
) : CustomTheme.Support {

    companion object {
        private const val TAG = "CTDrwblSelectorBuilder"
        private const val RECTANGLE = 0
        private const val OVAL = 1
        private const val RING = 2
        private const val LINE = 3
    }

    private var shape = RECTANGLE

    @AttrRes
    private var styleAttr: Int? = null

    private var defaultState = SelectorStateData()
    private var pressedState = SelectorStateData()
    private var selectedState = SelectorStateData()
    private var focusedState = SelectorStateData()
    private var unCheckedState = SelectorStateData()
    private var checkedState = SelectorStateData()
    private var disabledState = SelectorStateData()

    private val allStates = arrayOf(defaultState, pressedState, selectedState, focusedState,
        unCheckedState, checkedState, disabledState)

    private val gson: Gson by lazy { Gson() }

    fun rectangle() = apply { shape = RECTANGLE }
    fun oval() = apply { shape = OVAL }
    fun ring() = apply { shape = RING }
    fun line() = apply { shape = LINE }

    fun setStyle(@AttrRes attr: Int) = apply { styleAttr = attr }
    fun getStyle() = styleAttr
    fun hasStyle() = styleAttr != null

    fun hasDefaultDrawable() = defaultState.hasDrawable()
    fun setDefaultDrawable(drawable: Drawable?) = apply { defaultState.drawable = drawable }
    fun setDefaultDrawable(@DrawableRes resId: Int?) = apply { defaultState.drawableResId = resId }
    fun setDefaultDrawableAttr(@AttrRes attr: Int?) = apply { defaultState.drawableAttribute = attr }
    fun setDefaultDrawableTintColor(@ColorInt color: Int?) = apply { defaultState.drawableTintColorValue = color }
    fun setDefaultDrawableTintColorAttr(@AttrRes attr: Int?) = apply { defaultState.drawableTintColorAttribute = attr }
    fun setDefaultDrawableTintConfig(json: String) = apply { defaultState.drawableTintConfigJson = json }
    fun setDefaultColor(@ColorInt color: Int?) = apply { defaultState.colorValue = color }
    fun setDefaultColorAttr(@AttrRes attr: Int?) = apply { defaultState.colorAttribute = attr }
    fun setDefaultRadiusDp(value: Float) = apply { defaultState.setRadiusDp(value) }
    fun setDefaultStrokeWidthDp(value: Float) = apply { defaultState.strokeWidthDp = value }
    fun setDefaultStrokeColor(@ColorInt color: Int?) = apply { defaultState.strokeColorValue = color }
    fun setDefaultStrokeColorAttr(@AttrRes attr: Int?) = apply { defaultState.strokeColorAttribute = attr }
    fun setDefaultAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { defaultState.alpha = alpha }
    fun setDefaultWidthDp(value: Float) = apply { defaultState.widthDp = value }
    fun setDefaultHeightDp(value: Float) = apply { defaultState.heightDp = value }
    fun getDefaultAlpha() = defaultState.alpha ?: 1f
    fun getDefaultColor() = defaultState.colorValue
    fun getDefaultColorAttr() = defaultState.colorAttribute
    fun getDefaultDrawableTintColor() = defaultState.drawableTintColorValue
    fun getDefaultDrawableTintColorAttr() = defaultState.drawableTintColorAttribute

    fun setPressedDrawable(drawable: Drawable?) = apply { pressedState.drawable = drawable }
    fun setPressedDrawable(@DrawableRes resId: Int?) = apply { pressedState.drawableResId = resId }
    fun setPressedDrawableAttr(@AttrRes attr: Int?) = apply { pressedState.drawableAttribute = attr }
    fun setPressedDrawableTintColor(@ColorInt color: Int?) = apply { pressedState.drawableTintColorValue = color }
    fun setPressedDrawableTintColorAttr(@AttrRes attr: Int?) = apply { pressedState.drawableTintColorAttribute = attr }
    fun setPressedDrawableTintConfig(json: String) = apply { pressedState.drawableTintConfigJson = json }
    fun setPressedColor(@ColorInt color: Int?) = apply { pressedState.colorValue = color }
    fun setPressedColorAttr(@AttrRes attr: Int?) = apply { pressedState.colorAttribute = attr }
    fun setPressedRadiusDp(value: Float) = apply { pressedState.setRadiusDp(value) }
    fun setPressedStrokeWidthDp(value: Float) = apply { pressedState.strokeWidthDp = value }
    fun setPressedStrokeColor(@ColorInt color: Int?) = apply { pressedState.strokeColorValue = color }
    fun setPressedStrokeColorAttr(@AttrRes attr: Int?) = apply { pressedState.strokeColorAttribute = attr }
    fun setPressedAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { pressedState.alpha = alpha }
    fun setPressedWidthDp(value: Float) = apply { pressedState.widthDp = value }
    fun setPressedHeightDp(value: Float) = apply { pressedState.heightDp = value }

    fun setSelectedDrawable(drawable: Drawable?) = apply { selectedState.drawable = drawable }
    fun setSelectedDrawable(@DrawableRes resId: Int?) = apply { selectedState.drawableResId = resId }
    fun setSelectedDrawableAttr(@AttrRes attr: Int?) = apply { selectedState.drawableAttribute = attr }
    fun setSelectedDrawableTintColor(@ColorInt color: Int?) = apply { selectedState.drawableTintColorValue = color }
    fun setSelectedDrawableTintColorAttr(@AttrRes attr: Int?) = apply { selectedState.drawableTintColorAttribute = attr }
    fun setSelectedDrawableTintConfig(json: String) = apply { selectedState.drawableTintConfigJson = json }
    fun setSelectedColor(@ColorInt color: Int?) = apply { selectedState.colorValue = color }
    fun setSelectedColorAttr(@AttrRes attr: Int?) = apply { selectedState.colorAttribute = attr }
    fun setSelectedRadiusDp(value: Float) = apply { selectedState.setRadiusDp(value) }
    fun setSelectedStrokeWidthDp(value: Float) = apply { selectedState.strokeWidthDp = value }
    fun setSelectedStrokeColor(@ColorInt color: Int?) = apply { selectedState.strokeColorValue = color }
    fun setSelectedStrokeColorAttr(@AttrRes attr: Int?) = apply { selectedState.strokeColorAttribute = attr }
    fun setSelectedAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { selectedState.alpha = alpha }
    fun setSelectedWidthDp(value: Float) = apply { selectedState.widthDp = value }
    fun setSelectedHeightDp(value: Float) = apply { selectedState.heightDp = value }

    fun setFocusedDrawable(drawable: Drawable?) = apply { focusedState.drawable = drawable }
    fun setFocusedDrawable(@DrawableRes resId: Int?) = apply { focusedState.drawableResId = resId }
    fun setFocusedDrawableAttr(@AttrRes attr: Int?) = apply { focusedState.drawableAttribute = attr }
    fun setFocusedDrawableTintColor(@ColorInt color: Int?) = apply { focusedState.drawableTintColorValue = color }
    fun setFocusedDrawableTintColorAttr(@AttrRes attr: Int?) = apply { focusedState.drawableTintColorAttribute = attr }
    fun setFocusedDrawableTintConfig(json: String) = apply { focusedState.drawableTintConfigJson = json }
    fun setFocusedColor(@ColorInt color: Int?) = apply { focusedState.colorValue = color }
    fun setFocusedColorAttr(@AttrRes attr: Int?) = apply { focusedState.colorAttribute = attr }
    fun setFocusedRadiusDp(value: Float) = apply { focusedState.setRadiusDp(value) }
    fun setFocusedStrokeWidthDp(value: Float) = apply { focusedState.strokeWidthDp = value }
    fun setFocusedStrokeColor(@ColorInt color: Int?) = apply { focusedState.strokeColorValue = color }
    fun setFocusedStrokeColorAttr(@AttrRes attr: Int?) = apply { focusedState.strokeColorAttribute = attr }
    fun setFocusedAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { focusedState.alpha = alpha }
    fun setFocusedWidthDp(value: Float) = apply { focusedState.widthDp = value }
    fun setFocusedHeightDp(value: Float) = apply { focusedState.heightDp = value }

    fun setUnCheckedDrawable(drawable: Drawable?) = apply { unCheckedState.drawable = drawable }
    fun setUnCheckedDrawable(@DrawableRes resId: Int?) = apply { unCheckedState.drawableResId = resId }
    fun setUnCheckedDrawableAttr(@AttrRes attr: Int?) = apply { unCheckedState.drawableAttribute = attr }
    fun setUnCheckedDrawableTintColor(@ColorInt color: Int?) = apply { unCheckedState.drawableTintColorValue = color }
    fun setUnCheckedDrawableTintColorAttr(@AttrRes attr: Int?) = apply { unCheckedState.drawableTintColorAttribute = attr }
    fun setUnCheckedDrawableTintConfig(json: String) = apply { unCheckedState.drawableTintConfigJson = json }
    fun setUnCheckedColor(@ColorInt color: Int?) = apply { unCheckedState.colorValue = color }
    fun setUnCheckedColorAttr(@AttrRes attr: Int?) = apply { unCheckedState.colorAttribute = attr }
    fun setUnCheckedRadiusDp(value: Float) = apply { unCheckedState.setRadiusDp(value) }
    fun setUnCheckedStrokeWidthDp(value: Float) = apply { unCheckedState.strokeWidthDp = value }
    fun setUnCheckedStrokeColor(@ColorInt color: Int?) = apply { unCheckedState.strokeColorValue = color }
    fun setUnCheckedStrokeColorAttr(@AttrRes attr: Int?) = apply { unCheckedState.strokeColorAttribute = attr }
    fun setUnCheckedAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { unCheckedState.alpha = alpha }
    fun setUnCheckedWidthDp(value: Float) = apply { unCheckedState.widthDp = value }
    fun setUnCheckedHeightDp(value: Float) = apply { unCheckedState.heightDp = value }

    fun setCheckedDrawable(drawable: Drawable?) = apply { checkedState.drawable = drawable }
    fun setCheckedDrawable(@DrawableRes resId: Int?) = apply { checkedState.drawableResId = resId }
    fun setCheckedDrawableAttr(@AttrRes attr: Int?) = apply { checkedState.drawableAttribute = attr }
    fun setCheckedDrawableTintColor(@ColorInt color: Int?) = apply { checkedState.drawableTintColorValue = color }
    fun setCheckedDrawableTintColorAttr(@AttrRes attr: Int?) = apply { checkedState.drawableTintColorAttribute = attr }
    fun setCheckedDrawableTintConfig(json: String) = apply { checkedState.drawableTintConfigJson = json }
    fun setCheckedColor(@ColorInt color: Int?) = apply { checkedState.colorValue = color }
    fun setCheckedColorAttr(@AttrRes attr: Int?) = apply { checkedState.colorAttribute = attr }
    fun setCheckedRadiusDp(value: Float) = apply { checkedState.setRadiusDp(value) }
    fun setCheckedStrokeWidthDp(value: Float) = apply { checkedState.strokeWidthDp = value }
    fun setCheckedStrokeColor(@ColorInt color: Int?) = apply { checkedState.strokeColorValue = color }
    fun setCheckedStrokeColorAttr(@AttrRes attr: Int?) = apply { checkedState.strokeColorAttribute = attr }
    fun setCheckedAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { checkedState.alpha = alpha }
    fun setCheckedWidthDp(value: Float) = apply { checkedState.widthDp = value }
    fun setCheckedHeightDp(value: Float) = apply { checkedState.heightDp = value }

    fun setDisabledDrawable(drawable: Drawable?) = apply { disabledState.drawable = drawable }
    fun setDisabledDrawable(@DrawableRes resId: Int?) = apply { disabledState.drawableResId = resId }
    fun setDisabledDrawableAttr(@AttrRes attr: Int?) = apply { disabledState.drawableAttribute = attr }
    fun setDisabledDrawableTintColor(@ColorInt color: Int?) = apply { disabledState.drawableTintColorValue = color }
    fun setDisabledDrawableTintColorAttr(@AttrRes attr: Int?) = apply { disabledState.drawableTintColorAttribute = attr }
    fun setDisabledDrawableTintConfig(json: String) = apply { disabledState.drawableTintConfigJson = json }
    fun setDisabledColor(@ColorInt color: Int?) = apply { disabledState.colorValue = color }
    fun setDisabledColorAttr(@AttrRes attr: Int?) = apply { disabledState.colorAttribute = attr }
    fun setDisabledRadiusDp(value: Float) = apply { disabledState.setRadiusDp(value) }
    fun setDisabledStrokeWidthDp(value: Float) = apply { disabledState.strokeWidthDp = value }
    fun setDisabledStrokeColor(@ColorInt color: Int?) = apply { disabledState.strokeColorValue = color }
    fun setDisabledStrokeColorAttr(@AttrRes attr: Int?) = apply { disabledState.strokeColorAttribute = attr }
    fun setDisabledAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { disabledState.alpha = alpha }
    fun setDisabledWidthDp(value: Float) = apply { disabledState.widthDp = value }
    fun setDisabledHeightDp(value: Float) = apply { disabledState.heightDp = value }

    fun setRadiusDp(value: Float) = apply { allStates.map { it.setRadiusDp(value) } }
    fun setRadiusTopLeftDp(value: Float) = apply { allStates.map { it.setTopLeftRadiusDp(value) } }
    fun setRadiusTopRightDp(value: Float) = apply { allStates.map { it.setTopRightRadiusDp(value) } }
    fun setRadiusBottomLeftDp(value: Float) = apply { allStates.map { it.setBottomLeftRadiusDp(value) } }
    fun setRadiusBottomRightDp(value: Float) = apply { allStates.map { it.setBottomRightRadiusDp(value) } }

    fun setStrokeWidthDp(value: Float) = apply { allStates.map { it.strokeWidthDp = value } }
    fun setStrokeColor(@ColorInt color: Int?) = apply { allStates.map { it.strokeColorValue = color } }
    fun setStrokeColorAttr(@AttrRes attr: Int?) = apply { allStates.map { it.strokeColorAttribute = attr } }

    fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply { allStates.map { it.alpha = alpha } }

    fun setWidthDp(value: Float) = apply { allStates.map { it.widthDp = value } }
    fun setHeightDp(value: Float) = apply { allStates.map { it.heightDp = value } }

    private fun getColorByAttrName(context: Context, colorAttrName:String) : Int {
        val errorColor = Color.RED

        val colorAttr = try {
            context.resources.getIdentifier(colorAttrName, "attr", context.packageName)
        } catch (e: Exception) {
            Log.e(TAG, "getColorByAttrName - getIdentifier for attr name: $colorAttrName")
            -1
        }

        if (colorAttr == -1) {
            return errorColor
        }

        if (theme != null && theme?.isColor(colorAttr) == true) {
            return theme!!.getColor(colorAttr)
        }

        return try {
            CustomThemeApplier.getColor(context, colorAttr)
        } catch (e: Exception) {
            Log.e(TAG, "getColorByAttrName - getColor for attr name: $colorAttrName")
            errorColor
        }
    }

    private fun loadDrawable(state: SelectorStateData, resId: Int) : Drawable? {
        var drawable: Drawable? = null
        val context = if (theme != null) theme?.context else context

        context?.let { ctx ->
            if(state.hasTintConfig()) {
                drawable = VectorDrawableCompat.create(ctx.resources, resId, null)?.apply {
                    setAllowCaching(false)
                    state.getTintConfig(gson)?.let { config ->
                        config.items.forEach {
                            (getTargetByName(it.path) as? VectorDrawableCompat.VFullPath)?.let { path ->
                                path.fillColor = getColorByAttrName(ctx, it.colorAttr)
                            }
                        }
                    }
                }
            }

            if (drawable == null) {
                drawable = CustomThemeApplier.getDrawable(ctx, resId)
            }
        }

        return drawable
    }

    private fun getStateDrawable(state: SelectorStateData): Drawable? {
        state.drawable?.let {
            return it
        }

        state.drawableResId?.let {
            return loadDrawable(state, it)
        }

        state.drawableAttribute?.let {
            theme?.let { theme ->
                return CustomThemeApplier.getDrawableByAttr(theme, it)
            }
        }

        return null
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

    private fun buildDrawable(state: SelectorStateData): Drawable {
        val builder = CustomThemeDrawableBuilder(theme, context)
                .topLeftRadiusDp(state.getTopLeftRadiusDp())
                .topRightRadiusDp(state.getTopRightRadiusDp())
                .bottomLeftRadiusDp(state.getBottomLeftRadiusDp())
                .bottomRightRadiusDp(state.getBottomRightRadiusDp())

        when (shape) {
            OVAL -> builder.oval()
            RING -> builder.ring()
            LINE -> builder.line()
            else -> builder.rectangle()
        }

        if (theme != null && state.colorAttribute != null) {
            state.colorAttribute?.let { colorAttr ->
                when {
                    colorAttr == R.attr.auto -> {
                        getDefaultColorValue()?.let { color ->
                            builder.solidColor(CustomThemeApplier.getAutoColor(color))
                        }
                    }
                    theme!!.isColor(colorAttr) -> builder.solidColorAttr(colorAttr)
                    else -> {

                    }
                }
            }
        } else {
            state.colorValue?.let {
                builder.solidColor(it)
            }
        }

        if (state.hasStroke()) {
            builder.strokeWidthDp(state.strokeWidthDp!!)

            if (theme != null && state.strokeColorAttribute != null) {
                state.strokeColorAttribute?.let { colorAttr ->
                    when {
                        colorAttr == R.attr.auto -> {
                            getDefaultColorValue()?.let { color ->
                                builder.strokeColor(CustomThemeApplier.getAutoColor(color))
                            }
                        }
                        theme!!.isColor(colorAttr) -> builder.strokeColorAttr(colorAttr)
                        else -> {

                        }
                    }
                }
            } else {
                state.strokeColorValue?.let {
                    builder.strokeColor(it)
                }
            }
        }

        if (state.hasSize()) {
            builder.sizeDp(state.widthDp!!, state.heightDp!!)
        }

        state.alpha?.let {
            builder.alpha(it)
        }

        return builder.build()
    }

    private fun generateDrawable(state: SelectorStateData): Drawable? {
        if (state.isNotEmpty()) {
            getStateDrawable(state)?.let {
                var drawable: Drawable? = it

                if (state.hasTint()) {
                    var tintApplied = false

                    if (theme != null) {
                        state.drawableTintColorAttribute?.let { tintColorAttr ->
                            drawable = CustomThemeApplier.tint(theme!!, it, tintColorAttr)
                            tintApplied = true
                        }
                    }

                    if (!tintApplied) {
                        state.drawableTintColorValue?.let { tintColor ->
                            drawable = CustomThemeApplier.tint(it, tintColor)
                        }
                    }
                }

                if (state.hasSize()) {
                    drawable?.setBounds(0, 0, state.widthDp!!.dpToPx(context), state.heightDp!!.dpToPx(context))
                }

                state.alpha?.let { alphaValue ->
                    drawable?.alpha = (alphaValue * 255f).toInt()
                }

                return drawable
            }

            return buildDrawable(state)
        }

        return null
    }

    private fun createSelectorDrawable(defaultDrawable: Drawable, pressedDrawable: Drawable? = null,
                                       selectedDrawable: Drawable? = null, focusedDrawable: Drawable? = null,
                                       uncheckedDrawable: Drawable? = null, checkedDrawable: Drawable? = null,
                                       disabledDrawable: Drawable? = null): Drawable {
        return StateListDrawable().apply {
            pressedDrawable?.let {
                addState(intArrayOf(android.R.attr.state_pressed), it)
            }

            selectedDrawable?.let {
                addState(intArrayOf(android.R.attr.state_selected), it)
            }

            focusedDrawable?.let {
                addState(intArrayOf(android.R.attr.state_focused), it)
            }

            uncheckedDrawable?.let {
                addState(intArrayOf(-android.R.attr.state_checked), it)
            }

            checkedDrawable?.let {
                addState(intArrayOf(android.R.attr.state_checked), it)
            }

            disabledDrawable?.let {
                addState(intArrayOf(-android.R.attr.state_enabled), it)
            }

            addState(intArrayOf(), defaultDrawable)
        }
    }

    fun ready() = defaultState.isNotEmpty() || defaultState.hasTint()

    fun build(): Drawable? {
        if (defaultState.isNotEmpty()) {
            return createSelectorDrawable(
                    defaultDrawable = generateDrawable(defaultState)!!,
                    pressedDrawable = generateDrawable(pressedState),
                    selectedDrawable = generateDrawable(selectedState),
                    focusedDrawable = generateDrawable(focusedState),
                    uncheckedDrawable = generateDrawable(unCheckedState),
                    checkedDrawable = generateDrawable(checkedState),
                    disabledDrawable = generateDrawable(disabledState))
        }

        return null
    }

    fun getDrawableTintList() : ColorStateList? {
        if (ready()) {
            val builder = ColorStateListBuilder(theme)

            builder.addDrawableTintState(intArrayOf(android.R.attr.state_pressed), pressedState)
            builder.addDrawableTintState(intArrayOf(android.R.attr.state_selected), selectedState)
            builder.addDrawableTintState(intArrayOf(android.R.attr.state_focused), focusedState)
            builder.addDrawableTintState(intArrayOf(-android.R.attr.state_checked), unCheckedState)
            builder.addDrawableTintState(intArrayOf(android.R.attr.state_checked), checkedState)
            builder.addDrawableTintState(intArrayOf(-android.R.attr.state_enabled), disabledState)
            builder.addDrawableTintState(intArrayOf(android.R.attr.state_enabled), defaultState)
            builder.addDrawableTintState(intArrayOf(android.R.attr.state_window_focused), defaultState)
            builder.addDrawableTintState(intArrayOf(android.R.attr.state_accelerated), defaultState)
            builder.addDrawableTintState(intArrayOf(), defaultState)

            return builder.build()
        }

        return null
    }

    fun getThemeId() = theme?.id ?: -1

    override fun applyTheme(theme: CustomTheme) {
        this.theme = theme
    }
}