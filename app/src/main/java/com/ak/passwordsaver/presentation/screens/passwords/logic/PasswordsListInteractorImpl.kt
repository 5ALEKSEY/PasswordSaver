package com.ak.passwordsaver.presentation.screens.passwords.logic

import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.presentation.base.encryption.EncryptionUseCase
import com.ak.passwordsaver.presentation.screens.passwords.usecases.PasswordsListSortUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PasswordsListInteractorImpl @Inject constructor(
    val mPsDatabase: PSDatabase
) : IPasswordsListInteractor {

    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()
    private val mPasswordsListSortUserCase: PasswordsListSortUseCase = PasswordsListSortUseCase()

    override fun getAndListenAllPasswords(): Flowable<List<PasswordDBEntity>> {
        return getInvokedPasswordsUseCase()
            .flatMapSingle(this::getInvokedDecryptionUseCase)
            .map(mPasswordsListSortUserCase::descendingOrderById)
    }

    override fun getPasswordById(passwordId: Long) =
        getInvokedPasswordByIdUseCase(passwordId)
            .flatMap { entity -> getInvokedDecryptionUseCase(listOf(entity)) }
            .map { it[0] }

    override fun deletePasswordsById(passwordIds: List<Long>) =
        Observable.fromIterable(passwordIds)
            .map { passwordId ->
                PasswordDBEntity(passwordId)
            }
            .toList()
            .flatMap(this::getInvokedDeletePasswordsUseCase)
            .subscribeOn(Schedulers.io())

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

    private fun getInvokedPasswordsUseCase() =
        mPsDatabase.getPasswordsDao().getAllPasswords()
            .subscribeOn(Schedulers.io())

    private fun getInvokedPasswordByIdUseCase(passwordId: Long) =
        mPsDatabase.getPasswordsDao().getPasswordById(passwordId)
            .subscribeOn(Schedulers.io())

    private fun getInvokedDeletePasswordsUseCase(passwordIds: List<PasswordDBEntity>) =
        Single.fromCallable {
            val deletedRows = mPsDatabase.getPasswordsDao().deletePasswords(*passwordIds.toTypedArray())
            return@fromCallable deletedRows > 0
        }
            .subscribeOn(Schedulers.io())
}