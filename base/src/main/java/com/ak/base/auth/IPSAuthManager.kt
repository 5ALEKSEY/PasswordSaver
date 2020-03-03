package com.ak.base.auth

interface IPSAuthManager {
    fun isAppLocked(): Boolean
    fun setAppLockState(isLocked: Boolean)
    fun isLockAppSetAllowable(): Boolean
}