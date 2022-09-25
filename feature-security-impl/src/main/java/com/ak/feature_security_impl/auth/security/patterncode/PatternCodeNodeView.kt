package com.ak.feature_security_impl.auth.security.patterncode

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.feature_security_impl.R
import kotlinx.android.synthetic.main.layout_pattern_code_node_view.view.ivNodePatternView

// TODO: state must be implemented as sealed class
class PatternCodeNodeView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs), CustomTheme.Support {

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
        val drawableRes = theme.getDrawable(
            if (isPatternNodeEnabled) {
                R.attr.themedSecurityPatternNodeEnabledDrawable
            } else {
                R.attr.themedSecurityPatternNodeDisabledDrawable
            }
        )
        ivNodePatternView.setImageResource(drawableRes)
    }
}