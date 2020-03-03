package com.ak.base.ui

import moxy.MvpView

interface IBaseAppView : MvpView {
    fun showShortTimeMessage(message: String)
    fun invokeVibration(vibrateDuration: Long)
}