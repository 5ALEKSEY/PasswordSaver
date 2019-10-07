package com.ak.passwordsaver.presentation.screens.auth

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView

interface ISecurityView : IBaseAppView {
    fun showSecurityMessage(message: String, withAnimation: Boolean = false)
    fun showSuccessPatternAuthAction()
    fun showFailedPatternAuthAction()
    fun lockPatternViewInput()
    fun unlockPatternViewInput()
    fun finishActivityWithResult(isCanceled: Boolean)
}