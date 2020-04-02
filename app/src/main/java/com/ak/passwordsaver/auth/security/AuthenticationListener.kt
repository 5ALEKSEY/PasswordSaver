package com.ak.passwordsaver.auth.security

interface AuthenticationListener {
    fun onAuthSuccess()
    fun onAuthFailed()
    fun onAuthCancel()
}