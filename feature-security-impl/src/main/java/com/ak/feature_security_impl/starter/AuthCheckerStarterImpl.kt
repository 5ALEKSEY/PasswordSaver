package com.ak.feature_security_impl.starter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager
import com.ak.feature_security_impl.auth.SecurityActivity
import javax.inject.Inject

class AuthCheckerStarterImpl @Inject constructor(
    private val authManager: IPSAuthManager
) : IAuthCheckerStarter {

    companion object {
        private const val SECURITY_AUTH_ACTION_REQUEST_CODE = 55
        private const val SECURITY_CHANGE_ACTION_REQUEST_CODE = 56
    }

    private lateinit var callback: IAuthCheckerStarter.CheckAuthCallback

    override fun startAuthCheck(context: FragmentActivity, fragment: Fragment, securityAction: Int, callback: IAuthCheckerStarter.CheckAuthCallback) {
        this.callback = callback
        SecurityActivity.startSecurityForResult(context, fragment, securityAction, SECURITY_AUTH_ACTION_REQUEST_CODE)
    }

    override fun startAuthChange(context: FragmentActivity, fragment: Fragment, securityAction: Int) {
        SecurityActivity.startSecurityForResult(context, fragment, securityAction, SECURITY_CHANGE_ACTION_REQUEST_CODE)
    }

    override fun startAuthCheck(context: FragmentActivity, securityAction: Int, callback: IAuthCheckerStarter.CheckAuthCallback) {
        this.callback = callback
        SecurityActivity.startSecurityForResult(context, securityAction, SECURITY_AUTH_ACTION_REQUEST_CODE)
    }

    override fun startAuthChange(context: FragmentActivity, securityAction: Int) {
        SecurityActivity.startSecurityForResult(context, securityAction, SECURITY_CHANGE_ACTION_REQUEST_CODE)
    }

    override fun handleActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == SECURITY_AUTH_ACTION_REQUEST_CODE) {
            if (!this::callback.isInitialized) {
                throw IllegalStateException("Incorrect work of IAuthCheckerStarterImpl. not initialized IAuthCheckerStarter.CheckAuthCallback")
            }

            when (resultCode) {
                Activity.RESULT_OK -> {
                    authManager.setAppLockState(false)
                    callback.onAuthSuccessfully()
                }
                else -> callback.onAuthFailed()
            }
        }
    }
}