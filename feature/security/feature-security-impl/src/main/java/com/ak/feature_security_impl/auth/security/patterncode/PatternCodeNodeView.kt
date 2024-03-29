package com.ak.feature_security_impl.auth.security.patterncode

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeDrawableBuilder
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.feature_security_impl.R

// TODO: state must be implemented as sealed class
class PatternCodeNodeView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs), CustomTheme.Support {

    private val ivNodePatternView by lazy { findViewById<ImageView>(R.id.ivNodePatternView) }

    private var failedColor: Int? = null
    private var isPatternNodeEnabled = false

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_pattern_code_node_view, this)
        setNodeEnableState(isPatternNodeEnabled)
        initColors()
    }

    override fun setId(id: Int) {
        super.setId(View.generateViewId() + id)
    }

    override fun applyTheme(theme: CustomTheme) {
        initColors(theme)
        drawNodeState()
    }

    fun setNodeEnableState(isEnabled: Boolean) {
        isPatternNodeEnabled = isEnabled
        drawNodeState()
    }

    fun setNodeFailedState() {
        val drawable = ivNodePatternView.drawable
        drawable.setColorFilter(
            failedColor ?: return,
            PorterDuff.Mode.MULTIPLY
        )
    }

    private fun initColors(theme: CustomTheme = CustomThemeManager.getCurrentAppliedTheme()) {
        failedColor = theme.getColor(R.attr.themedErrorColor)
    }

    private fun drawNodeState(theme: CustomTheme = CustomThemeManager.getCurrentAppliedTheme()) {
        val nodeColorAttr = if (isPatternNodeEnabled) R.attr.themedAccentColor else R.attr.staticColorWhite
        val nodeRadiusPx = context.resources.getDimensionPixelSize(R.dimen.pattern_code_node_size)

        ivNodePatternView.setImageDrawable(
            CustomThemeDrawableBuilder(theme, context)
                .oval()
                .solidColorAttr(nodeColorAttr)
                .radius(nodeRadiusPx.toFloat())
                .build().apply {
                    setBounds(0, 0, nodeRadiusPx, nodeRadiusPx)
                }
        )
    }
}