package com.ak.core_repo_api.intefaces

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