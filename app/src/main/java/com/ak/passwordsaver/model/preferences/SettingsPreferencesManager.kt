package com.ak.passwordsaver.model.preferences

import com.ak.passwordsaver.model.PasswordShowingType

interface SettingsPreferencesManager {
    fun getPasswordShowingType(): PasswordShowingType
    fun setPasswordShowingType(passwordShowingType: PasswordShowingType)
    fun getStringListOfPasswordShowingTypes(): List<String>
}