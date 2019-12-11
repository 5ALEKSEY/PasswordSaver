package com.ak.passwordsaver.data.model.preferences.auth

import android.content.SharedPreferences
import com.ak.passwordsaver.di.modules.AppModule
import javax.inject.Inject
import javax.inject.Named

class AuthPreferencesManagerImpl @Inject constructor(
    @Named(AppModule.AUTH_PREFERENCES) private val authPreferences: SharedPreferences
) : IAuthPreferencesManager {

    companion object {
        const val IS_APP_LOCKED_SHARED_KEY = "is_application_locked"
    }

    override fun isApplicationLocked() =
        authPreferences.getBoolean(IS_APP_LOCKED_SHARED_KEY, false)

    override fun setApplicationLockState(isLocked: Boolean) {
        authPreferences.edit().putBoolean(IS_APP_LOCKED_SHARED_KEY, isLocked).apply()
    }
}