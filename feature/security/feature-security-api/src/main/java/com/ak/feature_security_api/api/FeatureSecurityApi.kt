package com.ak.feature_security_api.api

import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager
import com.ak.feature_security_api.interfaces.IPSBiometricManager

interface FeatureSecurityApi {
    fun provideAuthManager(): IPSAuthManager
    fun provideAuthChecker(): IAuthCheckerStarter
    fun providePSBiometricManager(): IPSBiometricManager
}