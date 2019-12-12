package com.ak.passwordsaver.presentation.base.managers.auth

import android.support.v7.app.AppCompatActivity

interface IPSAuthManager {
    fun setManagedForAuthActivity(activity: AppCompatActivity)
    fun setAuthFailedListener(listener: () -> Unit)
    fun startAuthAction()
    fun onAuthResultReceived(requestCode: Int, resultCode: Int)
    fun isAppLocked(): Boolean
    fun setAppLockState(isLocked: Boolean)
}