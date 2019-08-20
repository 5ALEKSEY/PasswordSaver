package com.ak.passwordsaver.presentation.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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