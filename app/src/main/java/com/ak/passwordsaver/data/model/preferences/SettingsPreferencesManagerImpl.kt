package com.ak.passwordsaver.data.model.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.data.model.PasswordShowingType
import javax.inject.Inject
import javax.inject.Named

class SettingsPreferencesManagerImpl @Inject constructor(
    @Named(AppModule.SETTINGS_PREFERENCES) val mSettingsPreferences: SharedPreferences,
    val mContext: Context
) : SettingsPreferencesManager {

    companion object {
        const val PASSWORD_SHOWING_TYPE_SHARED_KEY = "password_showing_type"
        const val IS_PINCODE_ENABLED_SHARED_KEY = "is_pincode_enabled"
        const val IS_PATTERN_ENABLED_SHARED_KEY = "is_pattern_enabled"
        const val PINCODE_VALUE_SHARED_KEY = "pincode_value"
        const val PATTERN_VALUE_SHARED_KEY = "pattern_value"
        const val BLOCK_SECURITY_INPUT_TIME_SHARED_KEY = "block_security_input_time"
    }

    override fun getPasswordShowingType() =
        PasswordShowingType.getTypeByNumber(mSettingsPreferences.getInt(PASSWORD_SHOWING_TYPE_SHARED_KEY, 0))

    override fun setPasswordShowingType(passwordShowingType: PasswordShowingType) {
        mSettingsPreferences.edit().putInt(PASSWORD_SHOWING_TYPE_SHARED_KEY, passwordShowingType.number).apply()
    }

    override fun getStringListOfPasswordShowingTypes() =
        PasswordShowingType.getListOfTypesMessages(mContext)

    override fun isPincodeEnabled() =
        mSettingsPreferences.getBoolean(IS_PINCODE_ENABLED_SHARED_KEY, false)

    override fun setPincodeEnableState(isEnabled: Boolean) {
        mSettingsPreferences.edit().putBoolean(IS_PINCODE_ENABLED_SHARED_KEY, isEnabled).apply()
    }

    override fun isPatternEnabled() =
        mSettingsPreferences.getBoolean(IS_PATTERN_ENABLED_SHARED_KEY, false)

    override fun setPatternEnableState(isEnabled: Boolean) {
        mSettingsPreferences.edit().putBoolean(IS_PATTERN_ENABLED_SHARED_KEY, isEnabled).apply()
    }

    override fun getUserPincodeValue() =
        mSettingsPreferences.getString(PINCODE_VALUE_SHARED_KEY, "")!!

    override fun setUserPincodeValue(newPincodeValue: String) {
        mSettingsPreferences.edit().putString(PINCODE_VALUE_SHARED_KEY, newPincodeValue).apply()
    }

    override fun getUserPatternValue() =
        mSettingsPreferences.getString(PATTERN_VALUE_SHARED_KEY, "")!!

    override fun setUserPatternValue(newPatternValue: String) {
        mSettingsPreferences.edit().putString(PATTERN_VALUE_SHARED_KEY, newPatternValue).apply()
    }

    override fun setBlockSecurityInputTime(blockSecurityInputTime: Long) {
        mSettingsPreferences.edit()
            .putLong(BLOCK_SECURITY_INPUT_TIME_SHARED_KEY, blockSecurityInputTime)
            .apply()
    }

    override fun getBlockSecurityInputTime() =
        mSettingsPreferences.getLong(BLOCK_SECURITY_INPUT_TIME_SHARED_KEY, 0L)
}