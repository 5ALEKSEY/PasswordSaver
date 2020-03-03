package com.ak.passwordsaver.domain.passwords

import com.ak.domain.data.model.db.entities.PasswordDBEntity
import com.ak.domain.data.repository.passwords.IPasswordsRepository
import com.ak.passwordsaver.domain.passwords.usecase.EncryptionUseCase
import com.ak.passwordsaver.domain.passwords.usecase.PasswordDataCheckUseCase
import com.ak.passwordsaver.domain.passwords.usecase.PasswordsListSortUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PasswordsInteractorImpl @Inject constructor(
    val passwordsRepository: IPasswordsRepository
) : IPasswordsInteractor {

    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()
    private val mPasswordsListSortUserCase: PasswordsListSortUseCase = PasswordsListSortUseCase()
    private val mPasswordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()

    override fun getAllPasswords(): Flowable<List<PasswordDBEntity>> =
        passwordsRepository.getAllPasswords()
            .flatMapSingle(this::getInvokedDecryptionUseCase)
            .map(mPasswordsListSortUserCase::descendingOrderById)

    override fun getPasswordById(passwordId: Long): Single<PasswordDBEntity> =
        passwordsRepository.getPasswordById(passwordId)
            .flatMap { entity -> getInvokedDecryptionUseCase(listOf(entity)) }
            .map { it[0] }

    override fun deletePasswordById(passwordId: Long): Single<Boolean> =
        passwordsRepository.deletePasswordsByIds(listOf(passwordId))

    override fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean> =
        passwordsRepository.deletePasswordsByIds(passwordIds)

    override fun addNewPassword(passwordDBEntity: PasswordDBEntity): Single<Boolean> =
        addNewPasswords(listOf(passwordDBEntity))

    override fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean> =
        getInvokedPasswordsDataCheckUseCase(passwordDBEntities)
            .flatMap { getInvokedEncryptionUseCase(passwordDBEntities) }
            .flatMap { encryptedPasswords -> passwordsRepository.addNewPasswords(encryptedPasswords) }

    override fun updatePassword(passwordDBEntity: PasswordDBEntity): Single<Boolean> =
        updatePasswords(listOf(passwordDBEntity))

    override fun updatePasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean> =
        getInvokedEncryptionUseCase(passwordDBEntities)
            .flatMap { encryptedEntities -> passwordsRepository.updatePasswords(encryptedEntities) }

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
            mEncryptionUseCase.decrypt(
                encryptedPasswordContent,
                { decryptedContent -> emitter.onSuccess(decryptedContent) },
                { throwable -> emitter.onError(throwable) }
            )
        }.toObservable()

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

    private fun getInvokedEncryptionUseCase(passwordDBEntities: List<PasswordDBEntity>) =
        Observable.fromIterable(passwordDBEntities)
            .flatMap(
                { entity -> encryptPasswordContent(entity.passwordContent) },
                { oldEntity, encryptedPassword ->
                    PasswordDBEntity(
                        oldEntity.passwordId,
                        oldEntity.passwordName,
                        oldEntity.passwordAvatarPath,
                        encryptedPassword
                    )
                })
            .toList()

    private fun encryptPasswordContent(passwordContent: String) =
        Single.create<String> { emitter ->
            mEncryptionUseCase.encrypt(
                passwordContent,
                { encryptedContent -> emitter.onSuccess(encryptedContent) },
                { throwable -> emitter.onError(throwable) }
            )
        }.toObservable()
}