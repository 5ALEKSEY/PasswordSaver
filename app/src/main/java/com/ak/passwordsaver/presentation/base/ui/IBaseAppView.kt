package com.ak.passwordsaver.presentation.base.ui

import com.arellomobile.mvp.MvpView

interface IBaseAppView : MvpView {
    fun showShortTimeMessage(message: String)
    fun invokeVibration(vibrateDuration: Long)
}