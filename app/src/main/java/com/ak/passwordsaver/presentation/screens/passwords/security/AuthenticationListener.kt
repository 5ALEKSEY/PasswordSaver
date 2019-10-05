package com.ak.passwordsaver.presentation.screens.passwords.security

interface AuthenticationListener {
    fun onAuthSuccess()
    fun onAuthFailed()
    fun onAuthCancel()
}