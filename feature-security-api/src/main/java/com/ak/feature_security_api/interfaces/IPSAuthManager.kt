package com.ak.feature_security_api.interfaces

interface IPSAuthManager {
    fun isAppLocked(): Boolean
    fun setAppLockState(isLocked: Boolean)
    fun isLockAppSetAllowable(): Boolean
}