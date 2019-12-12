package com.ak.passwordsaver.data.model.preferences.settings

import com.ak.passwordsaver.data.model.PasswordShowingType
import com.ak.passwordsaver.presentation.base.managers.auth.AppLockState

interface ISettingsPreferencesManager {
    fun getPasswordShowingType(): PasswordShowingType
    fun setPasswordShowingType(passwordShowingType: PasswordShowingType)
    fun getStringListOfPasswordShowingTypes(): List<String>
    fun getLockAppStatesList(): List<String>
    // Security
    fun isPincodeEnabled(): Boolean
    fun setPincodeEnableState(isEnabled: Boolean)
    fun isPatternEnabled(): Boolean
    fun setPatternEnableState(isEnabled: Boolean)
    fun getLockAppStateChoose(): AppLockState
    fun setLockAppStateChoose(appLockState: AppLockState)
    fun getUserPincodeValue(): String
    fun setUserPincodeValue(newPincodeValue: String)
    fun getUserPatternValue(): String
    fun setUserPatternValue(newPatternValue: String)
    fun setBlockSecurityInputTime(blockSecurityInputTime: Long)
    fun getBlockSecurityInputTime(): Long
}