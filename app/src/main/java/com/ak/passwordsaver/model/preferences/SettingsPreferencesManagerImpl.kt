package com.ak.passwordsaver.model.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ak.passwordsaver.di.modules.AppModule
import com.ak.passwordsaver.model.PasswordShowingType
import javax.inject.Inject
import javax.inject.Named

class SettingsPreferencesManagerImpl @Inject constructor(
    @Named(AppModule.SETTINGS_PREFERENCES) val mSettingsPreferences: SharedPreferences,
    val mContext: Context
) : SettingsPreferencesManager {

    companion object {
        const val PASSWORD_SHOWING_TYPE_SHARED_KEY = "password_showing_type"
    }

    override fun getPasswordShowingType() =
        PasswordShowingType.getTypeByNumber(mSettingsPreferences.getInt(PASSWORD_SHOWING_TYPE_SHARED_KEY, 0))

    override fun setPasswordShowingType(passwordShowingType: PasswordShowingType) {
        mSettingsPreferences.edit().putInt(PASSWORD_SHOWING_TYPE_SHARED_KEY, passwordShowingType.number).apply()
    }

    override fun getStringListOfPasswordShowingTypes() =
        PasswordShowingType.getListOfTypesMessages(mContext)
}