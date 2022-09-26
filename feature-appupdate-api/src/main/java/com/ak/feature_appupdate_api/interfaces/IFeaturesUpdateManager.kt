package com.ak.feature_appupdate_api.interfaces

import io.reactivex.rxjava3.core.Observable

interface IFeaturesUpdateManager {
    // tab account feature
    fun isTabAccountsFeatureViewed(): Boolean
    fun markTabAccountsFeatureAsViewed()

    // fingerprint unlock feature
    fun isFingerprintFeatureViewed(): Boolean
    fun markFingerprintFeatureAsViewed()

    // application theme design
    fun isAppThemeFeatureViewed(): Boolean
    fun markAppThemeFeatureAsViewed()

    fun subscribeToViewedFeatureState(featureType: FeatureType): Observable<Boolean>

    enum class FeatureType {
        TAB_ACCOUNTS,
        FINGERPRINT,
        APP_THEME,
    }
}