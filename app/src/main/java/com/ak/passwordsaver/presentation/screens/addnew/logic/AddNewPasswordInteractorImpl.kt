package com.ak.passwordsaver.presentation.screens.addnew.logic

import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.encryption.EncryptionUseCase
import com.ak.passwordsaver.presentation.screens.addnew.logic.usecases.PasswordDataCheckUseCase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddNewPasswordInteractorImpl @Inject constructor(
    val mPsDatabase: PSDatabase
) : AddNewPasswordInteractor {

    private val mPasswordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()
    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()

    override fun addNewPassword(passwordDBEntity: PasswordDBEntity) = addNewPasswords(listOf(passwordDBEntity))

    override fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean> {
        return getInvokedPasswordsDataCheckUseCase(passwordDBEntities)
            .flatMap { getInvokedEncryptionUseCase(passwordDBEntities) }
            .flatMap { encryptedPasswords -> getInvokedPasswordsInsertUseCase(encryptedPasswords) }
    }

    //------------------------------------------ Check passwords data --------------------------------------------------

    private fun getInvokedPasswordsDataCheckUseCase(passwordDBEntities: List<PasswordDBEntity>) =
        Observable.fromIterable(passwordDBEntities)
            .flatMap { entity -> checkPasswordData(entity.passwordName, entity.passwordContent) }
            .singleOrError()

    private fun checkPasswordData(passwordName: String, passwordContent: String) =
        Single.create<Boolean> { emitter ->
            try {
                mPasswordDataCheckUseCase.verifyPasswordData(passwordName, passwordContent)
                emitter.onSuccess(true)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.toObservable()

    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------ Encrypt passwords data ------------------------------------------------

    private fun getInvokedEncryptionUseCase(passwordDBEntities: List<PasswordDBEntity>) =
        Observable.fromIterable(passwordDBEntities)
            .flatMap(
                { entity -> encryptPasswordContent(entity.passwordContent) },
                { oldEntity, encryptedPassword ->
                    PasswordDBEntity(oldEntity.passwordName, encryptedPassword)
                })
            .toList()

    private fun encryptPasswordContent(passwordContent: String) =
        Single.create<String> { emitter ->
            try {
                mEncryptionUseCase.encrypt(passwordContent) {
                    emitter.onSuccess(it)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.toObservable()

    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------ Insert passwords data -------------------------------------------------

    private fun getInvokedPasswordsInsertUseCase(passwordDBEntities: List<PasswordDBEntity>) =
        Single.fromCallable { mPsDatabase.getPasswordsDao().insertNewPassword(*passwordDBEntities.toTypedArray()) }
            .subscribeOn(Schedulers.io())
            .map(this::checkPasswordsSuccessInsert)

    private fun checkPasswordsSuccessInsert(insertedRowsList: List<Long>) = insertedRowsList.size >= 0

    //------------------------------------------------------------------------------------------------------------------
}