package com.ak.passwordsaver.presentation.screens.home

import com.ak.domain.preferences.settings.ISettingsPreferencesManager
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class HomePresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : BasePSPresenter<IHomeView>() {

    companion object {
        private const val BACK_ACTION_CLICK_DELAY_IN_MILLIS = 1000L
    }

    private var mIsFinishScreenAllow = false

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun finishScreenAction() {
        if (mIsFinishScreenAllow) {
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
                mIsFinishScreenAllow = true
                viewState.showShortTimeMessage("Click again for exit app")
            }
            .subscribe { mIsFinishScreenAllow = false }
            .let(this::bindDisposable)
    }

    fun getSecureApplicationState() = settingsPreferencesManager.isPincodeEnabled()
}