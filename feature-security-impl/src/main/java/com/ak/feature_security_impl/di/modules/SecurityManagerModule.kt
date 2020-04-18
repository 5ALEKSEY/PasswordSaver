package com.ak.feature_security_impl.di.modules

import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_security_api.interfaces.IPSAuthManager
import com.ak.feature_security_impl.auth.PSAuthManagerImpl
import com.ak.feature_security_impl.starter.IAuthCheckerStarterImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SecurityManagerModule {
    @Binds
    @Singleton
    fun provideAuthAppManager(authAppManager: PSAuthManagerImpl): IPSAuthManager

    @Binds
    @Singleton
    fun provideAuthCheckerStarter(authCheckerStarterImpl: IAuthCheckerStarterImpl): IAuthCheckerStarter
}