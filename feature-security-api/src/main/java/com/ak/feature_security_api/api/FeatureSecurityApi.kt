package com.ak.feature_security_api.api

import com.ak.feature_security_api.interfaces.IPSAuthManager

interface FeatureSecurityApi {
    fun provideAuthManager(): IPSAuthManager
//    fun provideAuthChecker(): IAuthCheckerStarter
}