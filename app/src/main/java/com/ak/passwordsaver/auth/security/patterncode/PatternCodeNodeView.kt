package com.ak.passwordsaver.auth.security.patterncode

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ak.base.extensions.getColorCompat
import com.ak.passwordsaver.R
import kotlinx.android.synthetic.main.layout_pattern_code_node_view.view.*

class PatternCodeNodeView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val mFailedColor by lazy { context!!.getColorCompat(R.color.failed_action_color) }

    private val mInitPatterNodeEnableState = false
    private var mIsPatternNodeEnabled = mInitPatterNodeEnableState

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_pattern_code_node_view, this)
        setNodeEnableState(mIsPatternNodeEnabled)
    }

    fun setNodeEnableState(isEnabled: Boolean) {
        @DrawableRes val backgroundRes = getBackgroundDrawable(isEnabled)
        ivNodePatternView.setImageDrawable(context.getDrawable(backgroundRes))
        mIsPatternNodeEnabled = isEnabled
    }

    fun setNodeFailedState() {
        setPatternNodeColor(mFailedColor)
    }

    private fun setPatternNodeColor(@ColorInt color: Int) {
        val drawable = ivNodePatternView.drawable
        drawable.setColorFilter(
            color,
            PorterDuff.Mode.MULTIPLY
        )
    }

    @DrawableRes
    private fun getBackgroundDrawable(isEnabled: Boolean) =
        if (isEnabled) R.drawable.bg_enabled_pattern_node else R.drawable.bg_disabled_pattern_node
}