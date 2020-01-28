package com.ak.passwordsaver.presentation.base.ui

import moxy.MvpView

interface IBaseAppView : MvpView {
    fun showShortTimeMessage(message: String)
    fun invokeVibration(vibrateDuration: Long)
}