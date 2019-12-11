package com.ak.passwordsaver.presentation.base.managers.auth

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.ak.passwordsaver.data.model.preferences.auth.IAuthPreferencesManager
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import javax.inject.Inject

class PSAuthManagerImpl @Inject constructor(
    private val authPreferencesManager: IAuthPreferencesManager
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
                Activity.RESULT_OK -> authPreferencesManager.setApplicationLockState(false)
                else -> {
                    lockApplication()
                    mAuthFailedListener?.invoke()
                }
            }
        }
    }

    override fun isApplicationLocked() = authPreferencesManager.isApplicationLocked()

    override fun lockApplication() {
        authPreferencesManager.setApplicationLockState(true)
    }
}