package com.ak.passwordsaver.model.preferences

import com.ak.passwordsaver.model.PasswordShowingType

interface SettingsPreferencesManager {
    fun getPasswordShowingType(): PasswordShowingType
    fun setPasswordShowingType(passwordShowingType: PasswordShowingType)
    fun getStringListOfPasswordShowingTypes(): List<String>
    // Security
    fun isPincodeEnabled(): Boolean
    fun setPincodeEnableState(isEnabled: Boolean)
    fun isPatternEnabled(): Boolean
    fun setPatternEnableState(isEnabled: Boolean)
    fun getUserPincodeValue(): String
    fun setUserPincodeValue(newPincodeValue: String)
    fun getUserPatternValue(): String
    fun setUserPatternValue(newPatternValue: String)
    fun setBlockSecurityInputTime(blockSecurityInputTime: Long)
    fun getBlockSecurityInputTime(): Long
}