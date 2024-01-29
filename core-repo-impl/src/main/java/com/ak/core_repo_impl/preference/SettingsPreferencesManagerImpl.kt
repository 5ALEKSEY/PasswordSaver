package com.ak.core_repo_impl.preference

import android.content.Context
import android.content.SharedPreferences
import com.ak.core_repo_api.intefaces.AppLockState
import com.ak.core_repo_api.intefaces.AppLockStateHelper
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.core_repo_impl.di.module.PreferencesModule
import javax.inject.Inject
import javax.inject.Named

class SettingsPreferencesManagerImpl @Inject constructor(
    @Named(PreferencesModule.SETTINGS_PREFERENCES) private val settingsPreferences: SharedPreferences,
    private val mContext: Context
) : ISettingsPreferencesManager {

    private companion object {
        // TODO: Create preferences migrator
        @Deprecated("This one should be deleted")
        const val PASSWORD_SHOWING_TYPE_SHARED_KEY = "password_showing_type"

        // Security
        const val IS_PINCODE_ENABLED_SHARED_KEY = "is_pincode_enabled"
        const val IS_PATTERN_ENABLED_SHARED_KEY = "is_pattern_enabled"
        const val IS_BIOMETRIC_ENABLED_SHARED_KEY = "is_biometric_enabled"
        const val APP_LOCK_STATE_ID_SHARED_KEY = "app_lock_state_id"
        const val PINCODE_VALUE_SHARED_KEY = "pincode_value"
        const val PATTERN_VALUE_SHARED_KEY = "pattern_value"
        const val BLOCK_SECURITY_INPUT_TIME_SHARED_KEY = "block_security_input_time"

        // Design
        const val IS_CHANGE_THEME_WITH_ANIMATION_ENABLED_SHARED_KEY = "is_ch_theme_with_anim_enabled"
    }

    override fun getLockAppStatesList() = AppLockStateHelper.getAppLockStateStringList(mContext)

    override fun isPincodeEnabled() = settingsPreferences.getBoolean(IS_PINCODE_ENABLED_SHARED_KEY, false)

    override fun setPincodeEnableState(isEnabled: Boolean) {
        settingsPreferences.edit().putBoolean(IS_PINCODE_ENABLED_SHARED_KEY, isEnabled).apply()
    }

    override fun isPatternEnabled() =
        settingsPreferences.getBoolean(IS_PATTERN_ENABLED_SHARED_KEY, false)

    override fun setPatternEnableState(isEnabled: Boolean) {
        settingsPreferences.edit().putBoolean(IS_PATTERN_ENABLED_SHARED_KEY, isEnabled).apply()
    }

    override fun getLockAppStateChoose(): AppLockState {
        val lockStateId = settingsPreferences.getInt(APP_LOCK_STATE_ID_SHARED_KEY, 0)
        return AppLockStateHelper.convertFromLockStateId(lockStateId)
    }

    override fun setLockAppStateChoose(appLockState: AppLockState) {
        settingsPreferences.edit()
            .putInt(APP_LOCK_STATE_ID_SHARED_KEY, appLockState.lockStateId)
            .apply()
    }

    override fun getUserPincodeValue() =
        settingsPreferences.getString(PINCODE_VALUE_SHARED_KEY, "")!!

    override fun setUserPincodeValue(newPincodeValue: String) {
        settingsPreferences.edit().putString(PINCODE_VALUE_SHARED_KEY, newPincodeValue).apply()
    }

    override fun getUserPatternValue() =
        settingsPreferences.getString(PATTERN_VALUE_SHARED_KEY, "")!!

    override fun setUserPatternValue(newPatternValue: String) {
        settingsPreferences.edit().putString(PATTERN_VALUE_SHARED_KEY, newPatternValue).apply()
    }

    override fun isBiometricEnabled() =
        settingsPreferences.getBoolean(IS_BIOMETRIC_ENABLED_SHARED_KEY, false)

    override fun setBiometricEnableState(isEnabled: Boolean) {
        settingsPreferences.edit().putBoolean(IS_BIOMETRIC_ENABLED_SHARED_KEY, isEnabled).apply()
    }

    override fun setBlockSecurityInputTime(blockSecurityInputTime: Long) {
        settingsPreferences.edit()
            .putLong(BLOCK_SECURITY_INPUT_TIME_SHARED_KEY, blockSecurityInputTime)
            .apply()
    }

    override fun getBlockSecurityInputTime() = settingsPreferences.getLong(BLOCK_SECURITY_INPUT_TIME_SHARED_KEY, 0L)

    override fun isChangeThemeWithAnimationEnabled(): Boolean {
        return settingsPreferences.getBoolean(IS_CHANGE_THEME_WITH_ANIMATION_ENABLED_SHARED_KEY, false)
    }

    override fun setChangeThemeWithAnimationEnabledState(isEnabled: Boolean) {
        settingsPreferences.edit().putBoolean(IS_CHANGE_THEME_WITH_ANIMATION_ENABLED_SHARED_KEY, isEnabled).apply()
    }
}