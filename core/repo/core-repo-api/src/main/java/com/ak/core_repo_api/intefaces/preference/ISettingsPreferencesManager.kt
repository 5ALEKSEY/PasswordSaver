package com.ak.core_repo_api.intefaces.preference

import com.ak.core_repo_api.intefaces.AppLockState

interface ISettingsPreferencesManager {
    // Security
    fun getLockAppStatesList(): List<String>
    fun isPincodeEnabled(): Boolean
    fun setPincodeEnableState(isEnabled: Boolean)
    fun getUserPincodeValue(): String
    fun setUserPincodeValue(newPincodeValue: String)

    fun isPatternEnabled(): Boolean
    fun setPatternEnableState(isEnabled: Boolean)
    fun getUserPatternValue(): String
    fun setUserPatternValue(newPatternValue: String)

    fun isBiometricEnabled(): Boolean
    fun setBiometricEnableState(isEnabled: Boolean)

    fun getLockAppStateChoose(): AppLockState
    fun setLockAppStateChoose(appLockState: AppLockState)

    fun setBlockSecurityInputTime(blockSecurityInputTime: Long)
    fun getBlockSecurityInputTime(): Long

    // Design
    fun isChangeThemeWithAnimationEnabled(): Boolean
    fun setChangeThemeWithAnimationEnabledState(isEnabled: Boolean)
}