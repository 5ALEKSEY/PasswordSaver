package com.ak.feature_security_api.interfaces

interface IPSAuthManager {

    companion object RunActionType {
        const val AUTH_SECURITY_ACTION_TYPE = 1
        const val ADD_PINCODE_SECURITY_ACTION_TYPE = 2
        const val CHANGE_PINCODE_SECURITY_ACTION_TYPE = 3
        const val ADD_PATTERN_SECURITY_ACTION_TYPE = 4
        const val CHANGE_PATTERN_SECURITY_ACTION_TYPE = 5
    }

    fun isAppLocked(): Boolean
    fun setAppLockState(isLocked: Boolean)
    fun isLockAppSetAllowable(): Boolean
}