package com.ak.feature_appupdate_api.api

import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager

interface FeatureAppUpdateApi {
    fun provideFeaturesUpdateManager(): IFeaturesUpdateManager
}