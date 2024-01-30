package com.ak.feature_tabsettings_impl.di

import android.content.Context
import com.ak.app_theme.theme.ICustomThemesInitializer
import com.ak.base.scopes.FeatureScope
import dagger.Module
import dagger.Provides

@Module
class CustomThemesModule {

    @Provides
    @FeatureScope
    fun provideCustomThemesInitializer(appContext: Context): ICustomThemesInitializer {
        return appContext as? ICustomThemesInitializer
            ?: object : ICustomThemesInitializer {}
    }
}
