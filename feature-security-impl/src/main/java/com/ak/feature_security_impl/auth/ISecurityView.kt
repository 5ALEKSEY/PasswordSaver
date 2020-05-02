package com.ak.feature_security_impl.auth

import com.ak.base.ui.IBaseAppView

interface ISecurityView : IBaseAppView {
    fun showSecurityMessage(message: String, withAnimation: Boolean = false)
    fun hideSecurityMessage()
    fun showFailedPincodeAuthAction()
    fun showFailedPatternAuthAction()
    fun lockSecurityInputViews()
    fun unlockSecurityInputViews()
    fun setBiometricAuthVisibility(isBiometricVisible: Boolean)
    fun showBiometricFailedAttempt()
    fun setBiometricAuthLockState(isLocked: Boolean)
    fun sendAuthActionResult(isSuccessfully: Boolean)
    fun switchAuthMethod(isPincode: Boolean, withAnimation: Boolean = false)
    fun lockSwitchAuthMethod()
    fun unlockSwitchAuthMethod()
}