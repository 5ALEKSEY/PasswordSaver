package com.ak.passwordsaver.presentation.screens.auth

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class SecurityPresenter : BasePSPresenter<ISecurityView>() {

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun onPatterAuthFinished(patternCode: String) {
        // TODO: check pattern code and send result to view (hardcode below)
        Completable.timer(200L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState.sendAuthActionResult(false) }
            .let(this::bindDisposable)
    }

    fun onPincodeAuthFinished(pincodeResult: String) {

    }
}