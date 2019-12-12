package com.ak.passwordsaver.data.model.preferences.auth

interface IAuthPreferencesManager {
    fun isAppLocked(): Boolean
    fun saveAppLockState(isLocked: Boolean)
    fun saveAppLockTime(timeInMillis: Long)
    fun getAppLockTimeInMillis(): Long
}