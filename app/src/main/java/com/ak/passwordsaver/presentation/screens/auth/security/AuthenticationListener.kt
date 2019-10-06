package com.ak.passwordsaver.presentation.screens.auth.security

interface AuthenticationListener {
    fun onAuthSuccess()
    fun onAuthFailed()
    fun onAuthCancel()
}