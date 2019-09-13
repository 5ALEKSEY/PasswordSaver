package com.ak.passwordsaver.presentation.screens.addnew.logic

import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.encryption.EncryptionUseCase
import com.ak.passwordsaver.presentation.screens.addnew.logic.usecases.PasswordDataCheckUseCase
import io.reactivex.Single
import javax.inject.Inject

class AddNewPasswordInteractorImpl @Inject constructor(
    val mPsDatabase: PSDatabase
) : AddNewPasswordInteractor {

    private val mPasswordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()
    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()

    override fun addNewPassword(passwordName: String, passwordContent: String): Single<Boolean> {
        return getInvokedPasswordDataCheckUseCase(passwordName, passwordContent)
            .flatMap { getInvokedEncryptionUseCase(passwordContent) }
            .flatMap { encryptedPassword -> getPasswordInsertUseCase(PasswordDBEntity(passwordName, encryptedPassword)) }
    }

    private fun getInvokedEncryptionUseCase(passwordContent: String) =
        Single.create<String> { emitter ->
            try {
                mEncryptionUseCase.encrypt(passwordContent) {
                    emitter.onSuccess(it)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

    private fun getPasswordInsertUseCase(passwordDBEntity: PasswordDBEntity) =
        Single.fromCallable { mPsDatabase.getPasswordsDao().insertNewPassword(passwordDBEntity) }
            .map { longs -> longs.size >= 0 }

    private fun getInvokedPasswordDataCheckUseCase(passwordName: String, passwordContent: String) =
        Single.fromCallable {
            mPasswordDataCheckUseCase.verifyPasswordData(passwordName, passwordContent)
            return@fromCallable true
        }
}