package com.ak.core_repo_api.intefaces.preference

interface IAuthPreferencesManager {
    fun isAppLocked(): Boolean
    fun saveAppLockState(isLocked: Boolean)
    fun saveAppLockTime(timeInMillis: Long)
    fun getAppLockTimeInMillis(): Long
}