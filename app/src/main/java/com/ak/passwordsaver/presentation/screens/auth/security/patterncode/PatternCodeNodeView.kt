package com.ak.passwordsaver.presentation.screens.auth.security.patterncode

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView

class PatternCodeNodeView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val mFailedColor by lazy { ContextCompat.getColor(context!!, R.color.failed_action_color) }

    private val mPatterNodeView: ImageView by bindView(R.id.iv_node_pattern_view)

    private val mInitPatterNodeEnableState = false
    private var mIsPatternNodeEnabled = mInitPatterNodeEnableState

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_pattern_code_node_view, this)
        setNodeEnableState(mIsPatternNodeEnabled)
    }

    fun setNodeEnableState(isEnabled: Boolean) {
        @DrawableRes val backgroundRes = getBackgroundDrawable(isEnabled)
        mPatterNodeView.setImageDrawable(context.getDrawable(backgroundRes))
        mIsPatternNodeEnabled = isEnabled
    }

    fun setNodeFailedState() {
        setPatternNodeColor(mFailedColor)
    }

    private fun setPatternNodeColor(@ColorInt color: Int) {
        val drawable = mPatterNodeView.drawable
        drawable.setColorFilter(
            color,
            PorterDuff.Mode.MULTIPLY
        )
    }

    @DrawableRes
    private fun getBackgroundDrawable(isEnabled: Boolean) =
        if (isEnabled) R.drawable.bg_enabled_pattern_node else R.drawable.bg_disabled_pattern_node
}