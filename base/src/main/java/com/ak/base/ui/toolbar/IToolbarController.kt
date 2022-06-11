package com.ak.base.ui.toolbar

import androidx.annotation.MainThread
import androidx.annotation.StringRes

interface IToolbarController {
//    fun setup()
    @MainThread
    fun setToolbarTitle(title: String)
    @MainThread
    fun setToolbarTitle(@StringRes titleResIs: Int)
    @MainThread
    fun switchToolbarScrollingState(isScrollingEnabled: Boolean)
}