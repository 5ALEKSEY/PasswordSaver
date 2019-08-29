package com.ak.passwordsaver.presentation.base

import com.arellomobile.mvp.MvpView

interface IBaseAppView : MvpView {
    fun showShortTimeMessage(message: String)
}