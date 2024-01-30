package com.ak.feature_appupdate_api.interfaces

import io.reactivex.rxjava3.core.Observable

interface IFeaturesUpdateManager {
    // tab account feature
    fun isTabAccountsFeatureViewed(): Boolean
    fun markTabAccountsFeatureAsViewed()
    fun resetTabAccountsFeatureViewedState()

    // fingerprint unlock feature
    fun isFingerprintFeatureViewed(): Boolean
    fun markFingerprintFeatureAsViewed()
    fun resetFingerprintFeatureViewedState()

    // application theme design
    fun isAppThemeFeatureViewed(): Boolean
    fun markAppThemeFeatureAsViewed()
    fun resetAppThemeFeatureViewedState()

    // passwords and accounts backup
    fun isBackupFeatureViewed(): Boolean
    fun markBackupFeatureAsViewed()
    fun resetBackupFeatureViewedState()

    fun subscribeToViewedFeatureState(featureType: FeatureType): Observable<Boolean>

    enum class FeatureType {
        TAB_ACCOUNTS,
        FINGERPRINT,
        APP_THEME,
        BACKUP,
    }
}