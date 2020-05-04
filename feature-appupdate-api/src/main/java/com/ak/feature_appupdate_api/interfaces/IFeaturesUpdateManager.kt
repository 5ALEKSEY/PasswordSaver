package com.ak.feature_appupdate_api.interfaces

import io.reactivex.Observable

interface IFeaturesUpdateManager {
    // tab account feature
    fun isTabAccountsFeatureViewed(): Boolean
    fun markTabAccountsFeatureAsViewed()

    // fingerprint unlock feature
    fun isFingerprintFeatureViewed(): Boolean
    fun markFingerprintFeatureAsViewed()

    fun subscribeToViewedFeatureState(featureType: FeatureType): Observable<Boolean>

    enum class FeatureType {
        TAB_ACCOUNTS, FINGERPRINT
    }
}