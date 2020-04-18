package com.ak.feature_tabsettings_impl.di.modules

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @FeatureScope
    fun provideApplicationContext(): Context {
        return FeatureTabSettingsComponent.getAppContext()
    }
}