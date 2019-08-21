package com.ak.passwordsaver.presentation.screens.addnew

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class AddNewPasswordPresenter : BasePSPresenter<IAddNewPasswordView>() {

    @Inject
    lateinit var mDatabase: PSDatabase

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun saveNewPassword(name: String?, content: String?) {
        // TODO: create fields checkers
        Single.fromCallable {
            mDatabase.getPasswordsDao().insertNewPassword(PasswordDBEntity(null, name!!, null, content!!))
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { l ->
                    viewState.displaySuccessPasswordSave()
                },
                { throwable ->
                    viewState.displayFailedPasswordSave(throwable.message ?: "")
                })
            .let(::bindDisposable)
    }
}