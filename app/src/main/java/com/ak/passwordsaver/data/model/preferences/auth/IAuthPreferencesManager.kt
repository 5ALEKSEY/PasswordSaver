package com.ak.passwordsaver.data.model.preferences.auth

interface IAuthPreferencesManager {
    fun isApplicationLocked(): Boolean
    fun setApplicationLockState(isLocked: Boolean)
}