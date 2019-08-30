package com.ak.passwordsaver.presentation.screens.addnew.logic

import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.screens.addnew.logic.usecases.PasswordDataCheckException
import com.ak.passwordsaver.presentation.screens.addnew.logic.usecases.PasswordDataCheckUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddNewPasswordInteractorImpl @Inject constructor(
    val mPsDatabase: PSDatabase
) : AddNewPasswordInteractor {

    private val mPasswordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()

    override fun addNewPassword(passwordName: String, passwordContent: String): Single<Boolean> {
        try {
            mPasswordDataCheckUseCase.verifyPasswordData(passwordName, passwordContent)
        } catch (exception: PasswordDataCheckException) {
            return Single.error(exception)
        }

        return Single.fromCallable {
            mPsDatabase.getPasswordsDao().insertNewPassword(PasswordDBEntity(passwordName, passwordContent))
        }
            .subscribeOn(Schedulers.io())
            .map { longs -> longs.size >= 0 }
    }
}