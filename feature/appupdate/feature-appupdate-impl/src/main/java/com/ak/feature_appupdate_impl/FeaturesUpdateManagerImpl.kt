package com.ak.feature_appupdate_impl

import android.content.SharedPreferences
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_appupdate_impl.di.modules.PreferencesModule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class FeaturesUpdateManagerImpl @Inject constructor(
    @Named(PreferencesModule.FEATURES_UPDATE_PREFERENCES)
    private val featuresUpdateSP: SharedPreferences,
) : IFeaturesUpdateManager {

    companion object {
        private const val ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY = "is_acc_tab_feat_viewed"
        private const val FINGERPRINT_FEATURE_VIEWED_SHARED_KEY = "is_fingerprint_feat_viewed"
        private const val APP_THEME_FEATURE_VIEWED_SHARED_KEY = "is_app_theme_feat_viewed"
        private const val BACKUP_FEATURE_VIEWED_SHARED_KEY = "is_backup_feat_viewed"
    }

    private val featuresUpdateSubjectsMap = mapOf(
        IFeaturesUpdateManager.FeatureType.TAB_ACCOUNTS to BehaviorSubject.create<Boolean>(),
        IFeaturesUpdateManager.FeatureType.FINGERPRINT to BehaviorSubject.create(),
        IFeaturesUpdateManager.FeatureType.APP_THEME to BehaviorSubject.create(),
        IFeaturesUpdateManager.FeatureType.BACKUP to BehaviorSubject.create(),
    )

    override fun isTabAccountsFeatureViewed(): Boolean {
        return featuresUpdateSP.getBoolean(ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY, false)
    }

    override fun markTabAccountsFeatureAsViewed() {
        markFeatureAsViewed(
            featureSpKey = ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY,
            featureType = IFeaturesUpdateManager.FeatureType.TAB_ACCOUNTS,
        )
    }

    override fun resetTabAccountsFeatureViewedState() {
        featuresUpdateSP.edit().putBoolean(ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY, false).apply()
    }

    override fun isFingerprintFeatureViewed(): Boolean {
        return featuresUpdateSP.getBoolean(FINGERPRINT_FEATURE_VIEWED_SHARED_KEY, false)
    }

    override fun markFingerprintFeatureAsViewed() {
        markFeatureAsViewed(
            featureSpKey = FINGERPRINT_FEATURE_VIEWED_SHARED_KEY,
            featureType = IFeaturesUpdateManager.FeatureType.FINGERPRINT,
        )
    }

    override fun resetFingerprintFeatureViewedState() {
        featuresUpdateSP.edit().putBoolean(FINGERPRINT_FEATURE_VIEWED_SHARED_KEY, false).apply()
    }

    override fun isAppThemeFeatureViewed(): Boolean {
        return featuresUpdateSP.getBoolean(APP_THEME_FEATURE_VIEWED_SHARED_KEY, false)
    }

    override fun markAppThemeFeatureAsViewed() {
        markFeatureAsViewed(
            featureSpKey = APP_THEME_FEATURE_VIEWED_SHARED_KEY,
            featureType = IFeaturesUpdateManager.FeatureType.APP_THEME,
        )
    }

    override fun resetAppThemeFeatureViewedState() {
        featuresUpdateSP.edit().putBoolean(APP_THEME_FEATURE_VIEWED_SHARED_KEY, false).apply()
    }

    override fun isBackupFeatureViewed(): Boolean {
        return featuresUpdateSP.getBoolean(BACKUP_FEATURE_VIEWED_SHARED_KEY, false)
    }

    override fun markBackupFeatureAsViewed() {
        markFeatureAsViewed(
            featureSpKey = BACKUP_FEATURE_VIEWED_SHARED_KEY,
            featureType = IFeaturesUpdateManager.FeatureType.BACKUP,
        )
    }

    override fun resetBackupFeatureViewedState() {
        featuresUpdateSP.edit().putBoolean(BACKUP_FEATURE_VIEWED_SHARED_KEY, false).apply()
    }

    override fun subscribeToViewedFeatureState(featureType: IFeaturesUpdateManager.FeatureType): Observable<Boolean> {
        return featuresUpdateSubjectsMap[featureType] ?: throw NullPointerException("feature type is not initialized in map")
    }

    private fun markFeatureAsViewed(
        featureSpKey: String,
        featureType: IFeaturesUpdateManager.FeatureType,
    ) {
        featuresUpdateSP.edit().putBoolean(featureSpKey, true).apply()
        featuresUpdateSubjectsMap[featureType]?.onNext(true)
    }
}