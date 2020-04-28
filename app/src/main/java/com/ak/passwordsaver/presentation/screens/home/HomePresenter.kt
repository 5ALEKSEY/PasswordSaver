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
        if (!featuresUpdateManager.isTabAccountsFeatureViewed()) {
            viewState.setFeatureBadgeText(R.id.accounts_nav_graph, "New")
        }
    }

    fun onNavMenuDestinationChanged(destMenuId: Int) {
        when (destMenuId) {
            R.id.accounts_nav_graph -> {
                if (!featuresUpdateManager.isTabAccountsFeatureViewed()) {
                    featuresUpdateManager.markTabAccountsFeatureAsViewed()
                    viewState.removeFeatureBadgeText(destMenuId)
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
}