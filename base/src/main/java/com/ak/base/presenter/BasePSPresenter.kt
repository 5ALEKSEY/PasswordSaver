package com.ak.base.presenter

import android.content.Context
import com.ak.base.ui.IBaseAppView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import javax.inject.Inject

abstract class BasePSPresenter<View : IBaseAppView> : MvpPresenter<View>() {

    @Inject
    protected lateinit var applicationContext: Context

    private val mCompositeDisposable: CompositeDisposable by lazy(::CompositeDisposable)

    protected fun bindDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}