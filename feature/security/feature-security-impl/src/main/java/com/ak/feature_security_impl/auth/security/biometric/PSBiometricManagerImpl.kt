@file:Suppress("DEPRECATION")

package com.ak.feature_security_impl.auth.security.biometric

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.feature_security_api.interfaces.IPSBiometricManager
import javax.inject.Inject

class PSBiometricManagerImpl @Inject constructor(
    private val appContext: Context,
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : IPSBiometricManager {

    companion object {
        private const val TAG = "PSBiometricManagerImpl"
    }

    private var fingerprintManager: FingerprintManagerCompat? = null
    private var cancelSignal: CancellationSignal? = null

    override fun getBiometricFeatureAvailableStatus(): IPSBiometricManager.AvailableStatus {
        return when (BiometricManager.from(appContext).canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics.")
                IPSBiometricManager.AvailableStatus.AVAILABLE
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d(TAG, "No biometric features available on this device.")
                IPSBiometricManager.AvailableStatus.UNAVAILABLE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d(TAG, "Biometric features are currently unavailable.")
                IPSBiometricManager.AvailableStatus.UNAVAILABLE
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d(TAG, "The user hasn't associated " +
                        "any biometric credentials with their account.")
                IPSBiometricManager.AvailableStatus.NO_SAVED_FINGERPRINTS
            }
            else -> {
                Log.e(TAG, "Unknown canAuthenticate() return type. Returned UNAVAILABLE state")
                IPSBiometricManager.AvailableStatus.UNAVAILABLE
            }
        }
    }

    override fun isBiometricAuthEnabled() =
        getBiometricFeatureAvailableStatus() == IPSBiometricManager.AvailableStatus.AVAILABLE
                && settingsPreferencesManager.isBiometricEnabled()

    override fun startAuth(authCallback: IPSBiometricManager.AuthCallback) {
        fingerprintManager = FingerprintManagerCompat.from(appContext)
        cancelSignal = CancellationSignal()
        fingerprintManager?.authenticate(null, 0, cancelSignal, object : FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                super.onAuthenticationError(errMsgId, errString)
                if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                    // to many attempts
                    authCallback.onBiometricLocked()
                    cancelAuth()
                }
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                authCallback.onSucceeded()
            }

            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpMsgId, helpString)
                helpString?.toString()?.let {
                    authCallback.onHelpForUser(it)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                authCallback.onFailedAttempt()
            }
        }, null)
    }

    override fun cancelAuth() {
        cancelSignal?.cancel()
        fingerprintManager = null
        cancelSignal = null
    }
}