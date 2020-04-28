package com.ak.feature_appupdate_impl.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class PreferencesModule {

    companion object {
        const val FEATURES_UPDATE_PREFERENCES = "features_update_preferences"
    }

    @Provides
    @Singleton
    @Named(FEATURES_UPDATE_PREFERENCES)
    fun provideFeaturesUpdatePreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(FEATURES_UPDATE_PREFERENCES, Context.MODE_PRIVATE)
}