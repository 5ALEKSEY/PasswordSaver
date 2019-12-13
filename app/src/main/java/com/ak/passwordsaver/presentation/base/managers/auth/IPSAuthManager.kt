package com.ak.passwordsaver.presentation.base.managers.auth

interface IPSAuthManager {
    fun isAppLocked(): Boolean
    fun setAppLockState(isLocked: Boolean)
    fun isLockAppSetAllowable(): Boolean
}