package com.ak.domain.preferences.auth

import android.content.SharedPreferences
import com.ak.passwordsaver.di.modules.AppModule
import javax.inject.Inject
import javax.inject.Named

class AuthPreferencesManagerImpl @Inject constructor(
    @Named(AppModule.AUTH_PREFERENCES) private val authPreferences: SharedPreferences
) : IAuthPreferencesManager {

    companion object {
        private const val IS_APP_LOCKED_SHARED_KEY = "is_application_locked"
        private const val APP_LOCK_TIME_SHARED_KEY = "application_lock_time"
    }

    override fun isAppLocked() =
        authPreferences.getBoolean(IS_APP_LOCKED_SHARED_KEY, false)

    override fun saveAppLockState(isLocked: Boolean) {
        authPreferences.edit().putBoolean(IS_APP_LOCKED_SHARED_KEY, isLocked).apply()
    }

    override fun saveAppLockTime(timeInMillis: Long) {
        authPreferences.edit().putLong(APP_LOCK_TIME_SHARED_KEY, timeInMillis).apply()
    }

    override fun getAppLockTimeInMillis() =
        authPreferences.getLong(APP_LOCK_TIME_SHARED_KEY, 0L)
}