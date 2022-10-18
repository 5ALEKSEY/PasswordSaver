package com.ak.base.ui.toolbar

import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.StringRes

interface IToolbarController {
    @MainThread
    fun setToolbarTitle(@StringRes titleResIs: Int)
    @MainThread
    fun setToolbarTitle(title: String)
    @MainThread
    fun setupBackAction(@DrawableRes backIconResId: Int, action: () -> Unit)
    @MainThread
    fun clearBackAction()
    @MainThread
    fun startToolbarTitleLoading(@StringRes loadingTextResIs: Int)
    @MainThread
    fun startToolbarTitleLoading(loadingText: String)
    @MainThread
    fun stopToolbarTitleLoading()
}