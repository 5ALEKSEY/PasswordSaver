package com.ak.feature_appupdate_api.interfaces

interface IFeaturesUpdateManager {
    fun isTabAccountsFeatureViewed(): Boolean
    fun markTabAccountsFeatureAsViewed()
}