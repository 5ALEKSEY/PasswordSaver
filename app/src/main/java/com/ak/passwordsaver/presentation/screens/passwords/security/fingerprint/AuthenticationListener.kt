package com.ak.passwordsaver.presentation.screens.passwords.security.fingerprint

interface AuthenticationListener {
    fun onAuthSuccess()
    fun onAuthFailed()
    fun onAuthCancel()
}