package com.ak.feature_appupdate_impl

import android.content.SharedPreferences
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_appupdate_impl.di.modules.PreferencesModule
import javax.inject.Inject
import javax.inject.Named

class FeaturesUpdateManagerImpl @Inject constructor(
    @Named(PreferencesModule.FEATURES_UPDATE_PREFERENCES) private val featuresUpdateSP: SharedPreferences
) : IFeaturesUpdateManager {

    companion object {
        private const val ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY = "is_acc_tab_feat_viewed"
    }

    override fun isTabAccountsFeatureViewed() =
        featuresUpdateSP.getBoolean(ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY, false)

    override fun markTabAccountsFeatureAsViewed() {
        featuresUpdateSP.edit().putBoolean(ACCOUNT_TAB_FEATURE_VIEWED_SHARED_KEY, true).apply()
    }
}