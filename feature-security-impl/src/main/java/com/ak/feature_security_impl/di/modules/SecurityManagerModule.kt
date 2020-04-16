package com.ak.feature_security_impl.di.modules

import com.ak.feature_security_api.interfaces.IPSAuthManager
import com.ak.feature_security_impl.auth.PSAuthManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SecurityManagerModule {
    @Binds
    @Singleton
    fun provideAuthAppManager(authAppManager: PSAuthManagerImpl): IPSAuthManager
}