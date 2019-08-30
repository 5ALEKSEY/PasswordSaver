package com.ak.passwordsaver.presentation.screens.home

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class HomePresenter : BasePSPresenter<IHomeView>() {

    companion object {
        private const val BACK_ACTION_CLICK_DELAY_IN_MILLIS = 500L
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

        Completable.timer(BACK_ACTION_CLICK_DELAY_IN_MILLIS, TimeUnit.MILLISECONDS, Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mIsFinishScreenAllow = true
                viewState.showShortTimeMessage("Click again for exit app")
            }
            .subscribe { mIsFinishScreenAllow = false }
            .let(::bindDisposable)
    }
}