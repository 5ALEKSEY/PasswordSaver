package com.ak.feature_appupdate_impl

import android.content.SharedPreferences
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_appupdate_impl.di.modules.PreferencesModule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class FeaturesUpdateManagerImpl @Inject constructor(
    @Named(PreferencesModule.FEATURES_UPDATE_PREFERENCES) private val featuresUpdateSP: SharedPreferences
) : IFeaturesUpdateManager {

    companion object {
        private const val ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY = "is_acc_tab_feat_viewed"
        private const val FINGERPRINT_FEATURE_VIEWED_SHARED_KEY = "is_fingerprint_feat_viewed"
    }

    private val featuresUpdateSubjectsMap = mapOf(
        IFeaturesUpdateManager.FeatureType.TAB_ACCOUNTS to BehaviorSubject.create<Boolean>(),
        IFeaturesUpdateManager.FeatureType.FINGERPRINT to BehaviorSubject.create<Boolean>()
    )

    override fun isTabAccountsFeatureViewed() =
        featuresUpdateSP.getBoolean(ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY, false)

    override fun markTabAccountsFeatureAsViewed() {
        featuresUpdateSP.edit().putBoolean(ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY, true).apply()
        featuresUpdateSubjectsMap[IFeaturesUpdateManager.FeatureType.TAB_ACCOUNTS]?.onNext(true)
    }

    override fun isFingerprintFeatureViewed() =
        featuresUpdateSP.getBoolean(FINGERPRINT_FEATURE_VIEWED_SHARED_KEY, false)

    override fun markFingerprintFeatureAsViewed() {
        featuresUpdateSP.edit().putBoolean(FINGERPRINT_FEATURE_VIEWED_SHARED_KEY, true).apply()
        featuresUpdateSubjectsMap[IFeaturesUpdateManager.FeatureType.FINGERPRINT]?.onNext(true)
    }

    override fun subscribeToViewedFeatureState(featureType: IFeaturesUpdateManager.FeatureType): Observable<Boolean> {
        return featuresUpdateSubjectsMap[featureType] ?: throw NullPointerException("feature type is not initialized in map")
    }
}