package com.ak.passwordsaver.auth

interface IPSAuthManager {
    fun isAppLocked(): Boolean
    fun setAppLockState(isLocked: Boolean)
    fun isLockAppSetAllowable(): Boolean
}