package com.ak.passwordsaver.presentation.screens.passwords.security.patterncode

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.ak.passwordsaver.R

class PatternCodeNodeView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_pattern_code_node_view, this)
    }
}