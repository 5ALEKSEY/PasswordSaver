package com.ak.feature_appupdate_impl.di.modules

import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_appupdate_impl.FeaturesUpdateManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface FeatureAppUpdateManagersModule {

    @Binds
    @Singleton
    fun provideFeaturesUpdateManager(featuresUpdateManagerImpl: FeaturesUpdateManagerImpl): IFeaturesUpdateManager
}