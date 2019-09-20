package com.ak.passwordsaver.presentation.screens.passwords.logic

import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.encryption.EncryptionUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PasswordsListInteractorImpl @Inject constructor(
    val mPsDatabase: PSDatabase
) : PasswordsListInteractor {

    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()

    override fun getAndListenAllPasswords(): Flowable<List<PasswordDBEntity>> {
        return getInvokedPasswordsUseCase()
            .flatMapSingle(this::getInvokedDecryptionUseCase)
    }

    override fun getPasswordById(passwordId: Long) =
        getInvokedPasswordByIdUseCase(passwordId)
            .flatMap { entity -> getInvokedDecryptionUseCase(listOf(entity)) }
            .map { it[0] }

    //------------------------------------------ Decrypt passwords data ------------------------------------------------

    private fun getInvokedDecryptionUseCase(passwordDBEntities: List<PasswordDBEntity>) =
        Observable.fromIterable(passwordDBEntities)
            .flatMap(
                { entity -> decryptPasswordContent(entity.passwordContent) },
                { oldEntity, decryptedPasswordContent ->
                    oldEntity.passwordContent = decryptedPasswordContent
                    return@flatMap oldEntity
                })
            .toList()

    private fun decryptPasswordContent(encryptedPasswordContent: String) =
        Single.create<String> { emitter ->
            try {
                mEncryptionUseCase.decrypt(encryptedPasswordContent) {
                    emitter.onSuccess(it)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.toObservable()

    //------------------------------------------------------------------------------------------------------------------

    //---------------------------------------------- Get passwords data ------------------------------------------------

    private fun getInvokedPasswordsUseCase() =
        mPsDatabase.getPasswordsDao().getAllPasswords()
            .subscribeOn(Schedulers.io())

    private fun getInvokedPasswordByIdUseCase(passwordId: Long) =
        mPsDatabase.getPasswordsDao().getPasswordById(passwordId)
            .subscribeOn(Schedulers.io())

    //------------------------------------------------------------------------------------------------------------------
}