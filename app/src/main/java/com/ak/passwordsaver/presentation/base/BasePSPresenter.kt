package com.ak.passwordsaver.presentation.base

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter

abstract class BasePSPresenter<View : IBaseAppView> : MvpPresenter<View>() {

    private val mCompositeDisposable: CompositeDisposable by lazy(::CompositeDisposable)

    protected fun bindDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}