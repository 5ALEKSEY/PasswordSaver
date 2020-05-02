package com.ak.feature_security_api.interfaces

interface IPSBiometricManager {
    fun getBiometricFeatureAvailableStatus(): AvailableStatus
    fun isBiometricAuthEnabled(): Boolean
    fun startAuth(authCallback: AuthCallback)
    fun cancelAuth()

    enum class AvailableStatus {
        AVAILABLE, UNAVAILABLE, NO_SAVED_FINGERPRINTS
    }

    interface AuthCallback {
        fun onSucceeded() {}
        fun onFailedAttempt() {}
        fun onBiometricLocked() {}
        fun onHelpForUser(helpMessage: String) {}
    }
}