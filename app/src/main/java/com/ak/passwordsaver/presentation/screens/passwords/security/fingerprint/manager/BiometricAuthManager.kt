package com.ak.passwordsaver.presentation.screens.passwords.security.fingerprint.manager

import com.ak.passwordsaver.presentation.screens.passwords.security.fingerprint.AuthenticationListener

interface BiometricAuthManager {
    fun authenticate()
    fun cancelAuthenticate()
    fun attachAuthListener(authenticationListener: AuthenticationListener)
}