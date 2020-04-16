package com.ak.feature_security_impl.di.modules

import android.content.Context
import com.ak.feature_security_impl.di.FeatureSecurityComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext(): Context {
        return FeatureSecurityComponent.getAppContext()
    }
}