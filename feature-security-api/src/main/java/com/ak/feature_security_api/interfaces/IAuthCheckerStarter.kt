package com.ak.feature_security_api.interfaces

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface IAuthCheckerStarter {

    companion object RunActionType {
        const val AUTH_SECURITY_ACTION_TYPE = 1
        const val ADD_PINCODE_SECURITY_ACTION_TYPE = 2
        const val CHANGE_PINCODE_SECURITY_ACTION_TYPE = 3
        const val ADD_PATTERN_SECURITY_ACTION_TYPE = 4
        const val CHANGE_PATTERN_SECURITY_ACTION_TYPE = 5
    }

    fun startAuthCheck(context: FragmentActivity, fragment: Fragment, securityAction: Int, callback: CheckAuthCallback)
    fun startAuthChange(context: FragmentActivity, fragment: Fragment, securityAction: Int)

    fun startAuthCheck(context: FragmentActivity, securityAction: Int, callback: CheckAuthCallback)
    fun startAuthChange(context: FragmentActivity, securityAction: Int)

    fun handleActivityResult(requestCode: Int, resultCode: Int)

    interface CheckAuthCallback {
        fun onAuthSuccessfully() {}
        fun onAuthFailed() {}
    }
}