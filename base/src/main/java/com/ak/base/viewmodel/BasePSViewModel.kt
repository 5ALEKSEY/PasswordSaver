package com.ak.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BasePSViewModel: ViewModel() {

    private val disposables by lazy(::CompositeDisposable)

    protected val shortTimeMessageLiveData = MutableLiveData<String>()
    protected val vibrateLiveData = MutableLiveData<Long>()

    fun subscribeToShortTimeMessageLiveData(): LiveData<String> = shortTimeMessageLiveData
    fun subscribeToVibrateLiveData(): LiveData<Long> = vibrateLiveData

    protected fun bindDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}