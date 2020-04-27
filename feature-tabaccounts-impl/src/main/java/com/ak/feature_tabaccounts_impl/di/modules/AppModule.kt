package com.ak.feature_tabaccounts_impl.di.modules

import android.content.Context
import com.ak.base.scopes.FeatureScope
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @FeatureScope
    fun provideAppContext(): Context {
        return FeatureTabAccountsComponent.getAppContext()
    }
}