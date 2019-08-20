package com.ak.passwordsaver.presentation.screens.passwords

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class PasswordsListPresenter : BasePSPresenter<IPasswordsListView>() {

    @Inject
    lateinit var mDatabase: PSDatabase

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    public fun loadPasswords() {
        mDatabase.getPasswordsDao().getAllPasswords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    if (list.isEmpty()) {
                        viewState.displayEmptyPasswordsState()
                    }
                },
                { throwable ->

                })
            .let(::bindDisposable)
    }
}