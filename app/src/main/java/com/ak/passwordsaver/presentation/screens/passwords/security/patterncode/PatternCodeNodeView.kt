package com.ak.passwordsaver.presentation.screens.passwords.security.patterncode

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.vibrate

class PatternCodeNodeView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val mPatterNodeView: ImageView by bindView(R.id.iv_node_pattern_view)

    private val mInitPatterNodeEnableState = false
    private var mIsPatternNodeEnabled = mInitPatterNodeEnableState

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_pattern_code_node_view, this)
        setNodeEnableState(mIsPatternNodeEnabled)
    }

    fun setNodeEnableState(isEnabled: Boolean) {
        @DrawableRes val backgroundRes = getBackgroundDrawable(isEnabled)
        mPatterNodeView.setBackgroundResource(backgroundRes)
        mIsPatternNodeEnabled = isEnabled
        notifyEnableNodeState(mIsPatternNodeEnabled)
    }

    private fun notifyEnableNodeState(isEnabled: Boolean) {
        if (isEnabled) {
            context.vibrate(100)
        }
    }

    @DrawableRes
    private fun getBackgroundDrawable(isEnabled: Boolean) =
        if (isEnabled) R.drawable.bg_enabled_pattern_node else R.drawable.bg_disabled_pattern_node
}