package com.ak.passwordsaver.presentation.base.managers.auth

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.ak.passwordsaver.data.model.preferences.auth.IAuthPreferencesManager
import com.ak.passwordsaver.data.model.preferences.settings.ISettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import java.util.*
import javax.inject.Inject

class PSAuthManagerImpl @Inject constructor(
    private val authPreferencesManager: IAuthPreferencesManager,
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : IPSAuthManager {

    private var mManagedActivity: AppCompatActivity? = null
    private var mAuthFailedListener: (() -> Unit)? = null

    override fun setManagedForAuthActivity(activity: AppCompatActivity) {
        this.mManagedActivity = activity
    }

    override fun setAuthFailedListener(listener: () -> Unit) {
        this.mAuthFailedListener = listener
    }

    override fun startAuthAction() {
        if (mManagedActivity == null) {
            return
        }
        SecurityActivity.startSecurityForResult(
            mManagedActivity!!,
            SecurityPresenter.AUTH_SECURITY_ACTION_TYPE
        )
    }

    override fun onAuthResultReceived(requestCode: Int, resultCode: Int) {
        if (requestCode == AppConstants.SECURITY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> setAppLockState(false)
                else -> {
                    mAuthFailedListener?.invoke()
                }
            }
        }
    }

    override fun isAppLocked(): Boolean {
        val isAlreadyLocked = authPreferencesManager.isAppLocked()
                && settingsPreferencesManager.isPincodeEnabled()
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
}