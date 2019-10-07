package com.ak.passwordsaver.presentation.screens.auth.security

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.auth.ISecurityView
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
        viewState.showSuccessPatternAuthAction()
        Completable.timer(200L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState.finishActivityWithResult(false) }
            .let(this::bindDisposable)
    }
}