package com.ak.app_theme.theme.uicomponents

import androidx.annotation.AttrRes
import androidx.annotation.FloatRange

interface CustomThemeBackgroundSupport {
    fun setBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) alpha:Float) : Boolean
    fun setBackgroundTint(@AttrRes colorAttr: Int) : Boolean
}