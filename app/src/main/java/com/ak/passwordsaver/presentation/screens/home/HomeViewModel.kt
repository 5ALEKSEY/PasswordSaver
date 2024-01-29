package com.ak.passwordsaver.presentation.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.passwordsaver.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val resourceManager: IResourceManager
) : BasePSViewModel() {

    companion object {
        private const val BACK_ACTION_CLICK_DELAY_IN_MILLIS = 1000L
    }

    private val finishScreenLiveData = SingleEventLiveData<Unit>()
    private val featureBadgeTextLiveData = MutableLiveData<Pair<Int, Int?>>()

    private var isFinishScreenAllow = false

    fun subscribeToFinishScreenLiveData(): LiveData<Unit> = finishScreenLiveData
    fun subscribeToFeatureBadgeTextLiveData(): LiveData<Pair<Int, Int?>> = featureBadgeTextLiveData

    fun checkFeaturesBadgeUpdate() {
        // Accounts tab
        if (isAccountsTabWithNewBadge()) {
            featureBadgeTextLiveData.value = R.id.accounts_nav_graph to R.string.new_feature_badge
        }
        featuresUpdateManager.subscribeToViewedFeatureState(IFeaturesUpdateManager.FeatureType.TAB_ACCOUNTS)
            .subscribe {
                featureBadgeTextLiveData.value = R.id.accounts_nav_graph to null
            }
            .let(this::bindDisposable)

        // Passwords tab
        if (isPasswordsTabWithNewBadge()) {
            featureBadgeTextLiveData.value = R.id.passwords_nav_graph to R.string.new_feature_badge
        }

        // Settings tab
        fun deleteNewBadgeForSettingsTabIfNeeded() {
            if (!isSettingsTabWithNewBadge()) {
                featureBadgeTextLiveData.postValue(R.id.settings_nav_graph to null)
            }
        }
        if (isSettingsTabWithNewBadge()) {
            featureBadgeTextLiveData.value = R.id.settings_nav_graph to R.string.new_feature_badge
        }
        featuresUpdateManager.subscribeToViewedFeatureState(IFeaturesUpdateManager.FeatureType.FINGERPRINT)
            .subscribe(
                { deleteNewBadgeForSettingsTabIfNeeded() },
                {  }
            )
            .let(this::bindDisposable)
        featuresUpdateManager.subscribeToViewedFeatureState(IFeaturesUpdateManager.FeatureType.APP_THEME)
            .subscribe(
                { deleteNewBadgeForSettingsTabIfNeeded() },
                {  }
            )
            .let(this::bindDisposable)
        featuresUpdateManager.subscribeToViewedFeatureState(IFeaturesUpdateManager.FeatureType.BACKUP)
            .subscribe(
                { deleteNewBadgeForSettingsTabIfNeeded() },
                {  }
            )
            .let(this::bindDisposable)
    }

    fun onNavMenuDestinationChanged(destMenuId: Int) {
        when (destMenuId) {
            R.id.accounts_nav_graph -> {
                if (isAccountsTabWithNewBadge()) {
                    featuresUpdateManager.markTabAccountsFeatureAsViewed()
                }
            }
        }
    }

    fun finishScreenAction() {
        if (isFinishScreenAllow) {
            finishScreenLiveData.call()
            return
        }

        Completable.timer(
            BACK_ACTION_CLICK_DELAY_IN_MILLIS,
            TimeUnit.MILLISECONDS,
            Schedulers.computation()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isFinishScreenAllow = true
                shortTimeMessageLiveData.value = resourceManager.getString(R.string.exit_app_note_text)
            }
            .subscribe { isFinishScreenAllow = false }
            .let(this::bindDisposable)
    }

    fun getSecureApplicationState() = settingsPreferencesManager.isPincodeEnabled()

    private fun isAccountsTabWithNewBadge() = !featuresUpdateManager.isTabAccountsFeatureViewed()

    private fun isPasswordsTabWithNewBadge() = false

    private fun isSettingsTabWithNewBadge(): Boolean {
        return !featuresUpdateManager.isFingerprintFeatureViewed()
            || !featuresUpdateManager.isAppThemeFeatureViewed()
            || !featuresUpdateManager.isBackupFeatureViewed()
    }
}