package com.ak.passwordsaver.presentation.screens.passwords.security.fingerprint.manager

import android.content.Context
import android.os.CancellationSignal
import android.support.v4.app.FragmentActivity
import androidx.biometric.BiometricPrompt
import com.ak.passwordsaver.presentation.screens.passwords.security.fingerprint.AuthenticationListener
import java.util.concurrent.Executors

class BiometricAuthManagerImpl(private val context: Context) : BiometricAuthManager {

    private var mAuthenticationListener: AuthenticationListener? = null
    private lateinit var mBiometricPrompt: BiometricPrompt

    override fun authenticate() {
        val executor = Executors.newSingleThreadExecutor()
        val activity: FragmentActivity = this // reference to activity
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set the title to display.")
            .setSubtitle("Set the subtitle to display.")
            .setDescription("Set the description to display")
            .setNegativeButtonText("Negative Button")
            .build()

        mBiometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    TODO("Called when an unrecoverable error has been encountered and the operation is complete.")
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                TODO("Called when a biometric is recognized.")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                TODO("Called when a biometric is valid but not recognized.")
            }
        })

        mBiometricPrompt.authenticate(promptInfo)
    }

    override fun cancelAuthenticate() {

    }

    override fun attachAuthListener(authenticationListener: AuthenticationListener) {
        this.mAuthenticationListener = authenticationListener
    }

    private fun getCancellationSignal(): CancellationSignal {
        // With this cancel signal, we can cancel biometric prompt operation
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            mAuthenticationListener?.onAuthCancel()
        }
        return cancellationSignal
    }
}