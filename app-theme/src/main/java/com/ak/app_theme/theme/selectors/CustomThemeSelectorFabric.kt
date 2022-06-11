package com.ak.app_theme.theme.selectors

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.selectors.util.TypedUtils
import com.ak.app_theme.theme.selectors.util.TypedValue

internal object CustomThemeSelectorFabric {
    private const val TAG = "CTSelectorFabric"

    @AttrRes
    private fun getThemedStyleAttr(theme: CustomTheme?, attrs: AttributeSet?): Int? {
        if (theme != null && attrs != null) {
            val resourceValue = attrs.getAttributeValue(null, "style")
            if (!resourceValue.isNullOrEmpty() && resourceValue.startsWith("?")) {
                val styleId = resourceValue.substring(1).toInt()
                if (theme.isStyle(styleId)) {
                    return styleId
                }
            }
        }

        return null
    }

    fun getBackgroundSelector(
        context: Context, attrs: AttributeSet?,
        theme: CustomTheme? = null
    ): CustomThemeDrawableSelectorBuilder? {
        val typedValues = TypedUtils.obtainStyledAttributes(
            context, attrs,
            R.styleable.CustomThemeBackgroundSelectorAttributes, theme
        ) ?: return null

        val builder = CustomThemeDrawableSelectorBuilder(theme, context)

        setupBackgroundSelector(builder, typedValues)

        getThemedStyleAttr(theme, attrs)?.let { styleAttr ->
            TypedUtils.obtainStyledAttributes(
                theme!!, theme.getStyle(styleAttr),
                R.styleable.CustomThemeBackgroundSelectorAttributes
            )?.let {
                setupBackgroundSelector(builder, it)
                builder.setStyle(styleAttr)
            }
        }

        return if (builder.ready()) builder else null
    }

    fun getBackgroundSelector(
        theme: CustomTheme,
        styleAttr: Int
    ): CustomThemeDrawableSelectorBuilder? {
        if (theme.isStyle(styleAttr)) {
            TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeBackgroundSelectorAttributes
            )?.let {
                val builder = CustomThemeDrawableSelectorBuilder(theme)
                setupBackgroundSelector(builder, it)
                builder.setStyle(styleAttr)
                return if (builder.ready()) builder else null
            }
        }

        return null
    }

    private fun setupBackgroundSelector(
        builder: CustomThemeDrawableSelectorBuilder,
        typedValues: List<TypedValue>
    ) {
        typedValues.forEach {
            when (it.type) {
                TypedValue.TYPE_INT -> {
                    if (it.index == R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundShape) {
                        when (it.getInt()) {
                            1 -> builder.oval()
                            else -> builder.rectangle()
                        }
                    }
                }
                TypedValue.TYPE_FLOAT -> {
                    val alpha = it.getFloat()
                    if (alpha in 0f..1f) {
                        when (it.index) {
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundAlpha -> builder.setAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefaultAlpha -> builder.setDefaultAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressedAlpha -> builder.setPressedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelectedAlpha -> builder.setSelectedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocusedAlpha -> builder.setFocusedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnCheckedAlpha -> builder.setUnCheckedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundCheckedAlpha -> builder.setCheckedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabledAlpha -> builder.setDisabledAlpha(
                                alpha
                            )
                        }
                    }
                }
                TypedValue.TYPE_DIMENSION -> {
                    val dp = it.getDimension()
                    when (it.index) {
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundRadius -> builder.setRadiusDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundTopLeftRadius -> builder.setRadiusTopLeftDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundTopRightRadius -> builder.setRadiusTopRightDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundBottomLeftRadius -> builder.setRadiusBottomLeftDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundBottomRightRadius -> builder.setRadiusBottomRightDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundStrokeWidth -> builder.setStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefaultStrokeWidth -> builder.setDefaultStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressedStrokeWidth -> builder.setPressedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelectedStrokeWidth -> builder.setSelectedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocusedStrokeWidth -> builder.setFocusedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnCheckedStrokeWidth -> builder.setUnCheckedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundCheckedStrokeWidth -> builder.setCheckedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabledStrokeWidth -> builder.setDisabledStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundWidth -> builder.setWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundHeight -> builder.setHeightDp(
                            dp
                        )
                    }
                }
                else -> {
                    when (it.index) {
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setStrokeColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefault -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setDefaultDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setDefaultDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefaultTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefaultStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressed -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setPressedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setPressedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelected -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setSelectedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setSelectedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelectedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelectedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocused -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setFocusedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setFocusedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocusedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocusedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnChecked -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setUnCheckedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setUnCheckedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnCheckedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnCheckedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundChecked -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setCheckedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setCheckedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundCheckedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundCheckedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabled -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setDisabledDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setDisabledDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabledTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabledStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun applyBackgroundStyle(
        theme: CustomTheme, @AttrRes styleAttr: Int,
        builder: CustomThemeDrawableSelectorBuilder
    ): Boolean {
        if (theme.isStyle(styleAttr)) {
            val typedValues = TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeBackgroundSelectorAttributes
            ) ?: return false

            setupBackgroundSelector(builder, typedValues)
            return builder.ready()
        }

        return false
    }

    fun getColorBackgroundSelector(
        context: Context, attrs: AttributeSet?,
        theme: CustomTheme? = null
    ): CustomThemeColorSelectorBuilder? {
        val typedValues = TypedUtils.obtainStyledAttributes(
            context, attrs,
            R.styleable.CustomThemeBackgroundSelectorAttributes, theme
        ) ?: return null

        val builder = CustomThemeColorSelectorBuilder(theme)
        setupColorBackgroundSelector(builder, typedValues)

        getThemedStyleAttr(theme, attrs)?.let { styleAttr ->
            TypedUtils.obtainStyledAttributes(
                theme!!, theme.getStyle(styleAttr),
                R.styleable.CustomThemeBackgroundSelectorAttributes
            )?.let {
                setupColorBackgroundSelector(builder, it)
                builder.setStyle(styleAttr)
            }
        }

        return if (builder.ready()) builder else null
    }

    fun getColorBackgroundSelector(
        theme: CustomTheme,
        styleAttr: Int
    ): CustomThemeColorSelectorBuilder? {
        if (theme.isStyle(styleAttr)) {
            TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeBackgroundSelectorAttributes
            )?.let {
                val builder = CustomThemeColorSelectorBuilder(theme)
                setupColorBackgroundSelector(builder, it)
                builder.setStyle(styleAttr)
                return if (builder.ready()) builder else null
            }
        }

        return null
    }

    private fun setupColorBackgroundSelector(
        builder: CustomThemeColorSelectorBuilder,
        typedValues: List<TypedValue>
    ) {
        typedValues.forEach {
            when (it.type) {
                TypedValue.TYPE_COLOR -> {
                    when (it.index) {
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefault -> builder.setDefaultColor(
                            it.getColor()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressed -> builder.setPressedColor(
                            it.getColor()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelected -> builder.setSelectedColor(
                            it.getColor()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocused -> builder.setFocusedColor(
                            it.getColor()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnChecked -> builder.setUnCheckedColor(
                            it.getColor()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundChecked -> builder.setCheckedColor(
                            it.getColor()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabled -> builder.setDisabledColor(
                            it.getColor()
                        )
                    }
                }
                TypedValue.TYPE_COLOR_ATTRIBUTE -> {
                    when (it.index) {
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDefault -> builder.setDefaultColorAttr(
                            it.getResourceId()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundPressed -> builder.setPressedColorAttr(
                            it.getResourceId()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundSelected -> builder.setSelectedColorAttr(
                            it.getResourceId()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundFocused -> builder.setFocusedColorAttr(
                            it.getResourceId()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundUnChecked -> builder.setUnCheckedColorAttr(
                            it.getResourceId()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundChecked -> builder.setCheckedColorAttr(
                            it.getResourceId()
                        )
                        R.styleable.CustomThemeBackgroundSelectorAttributes_backgroundDisabled -> builder.setDisabledColorAttr(
                            it.getResourceId()
                        )
                    }
                }
                else -> {
                }
            }
        }
    }

    fun applyBackgroundStyle(
        theme: CustomTheme, @AttrRes styleAttr: Int,
        builder: CustomThemeColorSelectorBuilder
    ): Boolean {
        if (theme.isStyle(styleAttr)) {
            val typedValues = TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeBackgroundSelectorAttributes
            ) ?: return false

            setupColorBackgroundSelector(builder, typedValues)
            return builder.ready()
        }

        return false
    }

    fun getImageSelector(
        context: Context, attrs: AttributeSet?,
        theme: CustomTheme? = null
    ): CustomThemeDrawableSelectorBuilder? {
        val typedValues = TypedUtils.obtainStyledAttributes(
            context, attrs,
            R.styleable.CustomThemeImageSelectorAttributes, theme
        ) ?: return null

        val builder = CustomThemeDrawableSelectorBuilder(theme, context)
        setupImageSelector(builder, typedValues)

        getThemedStyleAttr(theme, attrs)?.let { styleAttr ->
            TypedUtils.obtainStyledAttributes(
                theme!!, theme.getStyle(styleAttr),
                R.styleable.CustomThemeImageSelectorAttributes
            )?.let {
                setupImageSelector(builder, it)
                builder.setStyle(styleAttr)
            }
        }

        return if (builder.ready()) builder else null
    }

    fun getImageSelector(theme: CustomTheme, styleAttr: Int): CustomThemeDrawableSelectorBuilder? {
        if (theme.isStyle(styleAttr)) {
            TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeImageSelectorAttributes
            )?.let {
                val builder = CustomThemeDrawableSelectorBuilder(theme)
                setupImageSelector(builder, it)
                builder.setStyle(styleAttr)
                return if (builder.ready()) builder else null
            }
        }

        return null
    }

    private fun setupImageSelector(
        builder: CustomThemeDrawableSelectorBuilder,
        typedValues: List<TypedValue>
    ) {
        typedValues.forEach {
            when (it.type) {
                TypedValue.TYPE_INT -> {
                    if (it.index == R.styleable.CustomThemeImageSelectorAttributes_srcShape) {
                        when (it.getInt()) {
                            1 -> builder.oval()
                            else -> builder.rectangle()
                        }
                    }
                }
                TypedValue.TYPE_FLOAT -> {
                    val alpha = it.getFloat()
                    if (alpha in 0f..1f) {
                        when (it.index) {
                            R.styleable.CustomThemeImageSelectorAttributes_srcAlpha -> builder.setAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcDefaultAlpha -> builder.setDefaultAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcPressedAlpha -> builder.setPressedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcSelectedAlpha -> builder.setSelectedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcFocusedAlpha -> builder.setFocusedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcUnCheckedAlpha -> builder.setUnCheckedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcCheckedAlpha -> builder.setCheckedAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeImageSelectorAttributes_srcDisabledAlpha -> builder.setDisabledAlpha(
                                alpha
                            )
                        }
                    }
                }
                TypedValue.TYPE_DIMENSION -> {
                    val dp = it.getDimension()
                    when (it.index) {
                        R.styleable.CustomThemeImageSelectorAttributes_srcRadius -> builder.setRadiusDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcTopLeftRadius -> builder.setRadiusTopLeftDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcTopRightRadius -> builder.setRadiusTopRightDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcBottomLeftRadius -> builder.setRadiusBottomLeftDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcBottomRightRadius -> builder.setRadiusBottomRightDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcStrokeWidth -> builder.setStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcDefaultStrokeWidth -> builder.setDefaultStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcPressedStrokeWidth -> builder.setPressedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcSelectedStrokeWidth -> builder.setSelectedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcFocusedStrokeWidth -> builder.setFocusedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcUnCheckedStrokeWidth -> builder.setUnCheckedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcCheckedStrokeWidth -> builder.setCheckedStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcDisabledStrokeWidth -> builder.setDisabledStrokeWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcWidth -> builder.setWidthDp(
                            dp
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcHeight -> builder.setHeightDp(
                            dp
                        )
                    }
                }
                TypedValue.TYPE_STRING -> {
                    when (it.index) {
                        R.styleable.CustomThemeImageSelectorAttributes_srcDefaultTintConfig -> builder.setDefaultDrawableTintConfig(
                            it.getString()
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcPressedTintConfig -> builder.setPressedDrawableTintConfig(
                            it.getString()
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcSelectedTintConfig -> builder.setSelectedDrawableTintConfig(
                            it.getString()
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcFocusedTintConfig -> builder.setFocusedDrawableTintConfig(
                            it.getString()
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcUnCheckedTintConfig -> builder.setUnCheckedDrawableTintConfig(
                            it.getString()
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcCheckedTintConfig -> builder.setCheckedDrawableTintConfig(
                            it.getString()
                        )
                        R.styleable.CustomThemeImageSelectorAttributes_srcDisabledTintConfig -> builder.setDisabledDrawableTintConfig(
                            it.getString()
                        )
                    }
                }
                else -> {
                    when (it.index) {
                        R.styleable.CustomThemeImageSelectorAttributes_srcDefault -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setDefaultDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setDefaultDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcDefaultTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcDefaultStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcPressed -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setPressedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setPressedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcPressedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcPressedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcSelected -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setSelectedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setSelectedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcSelectedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcSelectedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcFocused -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setFocusedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setFocusedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcFocusedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcFocusedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcUnChecked -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setUnCheckedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setUnCheckedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcUnCheckedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcUnCheckedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcChecked -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setCheckedDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setCheckedDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcCheckedTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcCheckedStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcDisabled -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledColorAttr(it.getResourceId())
                                TypedValue.TYPE_DRAWABLE_ATTRIBUTE -> builder.setDisabledDrawableAttr(
                                    it.getResourceId()
                                )
                                TypedValue.TYPE_DRAWABLE -> builder.setDisabledDrawable(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcDisabledTint -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledDrawableTintColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledDrawableTintColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                        R.styleable.CustomThemeImageSelectorAttributes_srcDisabledStrokeColor -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledStrokeColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledStrokeColorAttr(
                                    it.getResourceId()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun applyImageStyle(
        theme: CustomTheme, @AttrRes styleAttr: Int,
        builder: CustomThemeDrawableSelectorBuilder
    ): Boolean {
        if (theme.isStyle(styleAttr)) {
            val typedValues = TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeImageSelectorAttributes
            ) ?: return false

            setupImageSelector(builder, typedValues)
            return builder.ready()
        }

        return false
    }

    fun getTextSelector(
        context: Context, attrs: AttributeSet?,
        theme: CustomTheme? = null
    ): CustomThemeColorSelectorBuilder? {
        val typedValues = TypedUtils.obtainStyledAttributes(
            context, attrs,
            R.styleable.CustomThemeTextSelectorAttributes, theme
        ) ?: return null

        val builder = CustomThemeColorSelectorBuilder(theme)
        setupTextSelector(builder, typedValues)

        getThemedStyleAttr(theme, attrs)?.let { styleAttr ->
            TypedUtils.obtainStyledAttributes(
                theme!!, theme.getStyle(styleAttr),
                R.styleable.CustomThemeTextSelectorAttributes
            )?.let {
                setupTextSelector(builder, it)
                builder.setStyle(styleAttr)
            }
        }

        return if (builder.ready()) builder else null
    }

    fun getTextSelector(theme: CustomTheme, styleAttr: Int): CustomThemeColorSelectorBuilder? {
        if (theme.isStyle(styleAttr)) {
            TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeTextSelectorAttributes
            )?.let {
                val builder = CustomThemeColorSelectorBuilder(theme)
                setupTextSelector(builder, it)
                builder.setStyle(styleAttr)
                return if (builder.ready()) builder else null
            }
        }

        return null
    }

    private fun setupTextSelector(
        builder: CustomThemeColorSelectorBuilder,
        typedValues: List<TypedValue>
    ) {
        typedValues.forEach {
            when (it.type) {
                TypedValue.TYPE_FLOAT -> {
                    val alpha = it.getFloat()
                    if (alpha in 0f..1f) {
                        when (it.index) {
                            R.styleable.CustomThemeTextSelectorAttributes_textAlpha -> builder.setAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorDefaultAlpha -> builder.setDefaultColorAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorPressedAlpha -> builder.setPressedColorAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorSelectedAlpha -> builder.setSelectedColorAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorFocusedAlpha -> builder.setFocusedColorAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorUnCheckedAlpha -> builder.setUnCheckedColorAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorCheckedAlpha -> builder.setCheckedColorAlpha(
                                alpha
                            )
                            R.styleable.CustomThemeTextSelectorAttributes_textColorDisabledAlpha -> builder.setDisabledColorAlpha(
                                alpha
                            )
                        }
                    }
                }
                else -> {
                    when (it.index) {
                        R.styleable.CustomThemeTextSelectorAttributes_textColorDefault -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDefaultColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDefaultColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeTextSelectorAttributes_textColorPressed -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setPressedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setPressedColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeTextSelectorAttributes_textColorSelected -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setSelectedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setSelectedColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeTextSelectorAttributes_textColorFocused -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setFocusedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setFocusedColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeTextSelectorAttributes_textColorUnChecked -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setUnCheckedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setUnCheckedColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeTextSelectorAttributes_textColorChecked -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setCheckedColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setCheckedColorAttr(it.getResourceId())
                            }
                        }
                        R.styleable.CustomThemeTextSelectorAttributes_textColorDisabled -> {
                            when (it.type) {
                                TypedValue.TYPE_COLOR -> builder.setDisabledColor(it.getColor())
                                TypedValue.TYPE_COLOR_ATTRIBUTE -> builder.setDisabledColorAttr(it.getResourceId())
                            }
                        }
                    }
                }
            }
        }
    }

    fun applyTextStyle(
        theme: CustomTheme, @AttrRes styleAttr: Int,
        builder: CustomThemeColorSelectorBuilder
    ): Boolean {
        if (theme.isStyle(styleAttr)) {
            val typedValues = TypedUtils.obtainStyledAttributes(
                theme, theme.getStyle(styleAttr),
                R.styleable.CustomThemeTextSelectorAttributes
            ) ?: return false

            setupTextSelector(builder, typedValues)
            return builder.ready()
        }

        return false
    }
}
