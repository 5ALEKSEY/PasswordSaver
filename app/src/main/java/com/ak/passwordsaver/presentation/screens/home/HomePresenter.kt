package com.ak.passwordsaver.presentation.screens.home

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.di.AppComponent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class HomePresenter @Inject constructor(
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : BasePSPresenter<IHomeView>() {

    companion object {
        private const val BACK_ACTION_CLICK_DELAY_IN_MILLIS = 1000L
    }

    private var isFinishScreenAllow = false

    init {
        AppComponent.get().inject(this)
    }

    fun checkFeaturesBadgeUpdate() {
        if (isAccountsTabWithNewBadge()) {
            viewState.setFeatureBadgeText(R.id.accounts_nav_graph, "New")
        }
        featuresUpdateManager.subscribeToViewedFeatureState(IFeaturesUpdateManager.FeatureType.TAB_ACCOUNTS)
            .subscribe {
                viewState.removeFeatureBadgeText(R.id.accounts_nav_graph)
            }
            .let(this::bindDisposable)

        if (isPasswordsTabWithNewBadge()) {
            viewState.setFeatureBadgeText(R.id.passwords_nav_graph, "New")
        }

        if (isSettingsTabWithNewBadge()) {
            viewState.setFeatureBadgeText(R.id.settings_nav_graph, "New")
        }
        featuresUpdateManager.subscribeToViewedFeatureState(IFeaturesUpdateManager.FeatureType.FINGERPRINT)
            .subscribe {
                viewState.removeFeatureBadgeText(R.id.settings_nav_graph)
            }
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
            viewState.finishScreen()
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
                viewState.showShortTimeMessage("Click again for exit app")
            }
            .subscribe { isFinishScreenAllow = false }
            .let(this::bindDisposable)
    }

    fun getSecureApplicationState() = settingsPreferencesManager.isPincodeEnabled()

    private fun isAccountsTabWithNewBadge() = !featuresUpdateManager.isTabAccountsFeatureViewed()

    private fun isPasswordsTabWithNewBadge() = false

    private fun isSettingsTabWithNewBadge() = !featuresUpdateManager.isFingerprintFeatureViewed()
}