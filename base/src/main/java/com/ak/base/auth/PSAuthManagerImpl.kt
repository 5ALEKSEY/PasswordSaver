package com.ak.base.auth

import com.ak.passwordsaver.data.model.preferences.auth.IAuthPreferencesManager
import com.ak.passwordsaver.data.model.preferences.settings.ISettingsPreferencesManager
import java.util.*
import javax.inject.Inject

class PSAuthManagerImpl @Inject constructor(
    private val authPreferencesManager: IAuthPreferencesManager,
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : IPSAuthManager {

    override fun isAppLocked(): Boolean {
        val isAlreadyLocked = authPreferencesManager.isAppLocked() && isAppSecureEnabled()
        val startLockTimeInMillis = authPreferencesManager.getAppLockTimeInMillis()
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        val delayAfterLock = currentTimeInMillis - startLockTimeInMillis
        val allowableDelay = settingsPreferencesManager.run {
            return@run getLockAppStateChoose().getLockStateDelayInMillis()
        }

        return isAlreadyLocked && startLockTimeInMillis != 0L && delayAfterLock > allowableDelay
    }

    override fun setAppLockState(isLocked: Boolean) {
        authPreferencesManager.apply {
            saveAppLockState(isLocked)
            saveAppLockTime(if (isLocked) Calendar.getInstance().timeInMillis else 0L)
        }
    }

    override fun isLockAppSetAllowable() = isAppSecureEnabled() && !isAppLocked()

    private fun isAppSecureEnabled() = settingsPreferencesManager.isPincodeEnabled()
}