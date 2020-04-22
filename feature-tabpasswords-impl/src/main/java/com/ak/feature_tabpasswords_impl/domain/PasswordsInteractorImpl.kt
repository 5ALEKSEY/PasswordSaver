package com.ak.feature_tabpasswords_impl.domain

import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.domain.entity.mapFeatureToDomainEntitiesList
import com.ak.feature_tabpasswords_impl.domain.entity.mapRepoToDomainEntitiesList
import com.ak.feature_tabpasswords_impl.domain.usecase.EncryptionUseCase
import com.ak.feature_tabpasswords_impl.domain.usecase.PasswordDataCheckUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PasswordsInteractorImpl @Inject constructor(
    private val passwordsRepository: IPasswordsRepository
) : IPasswordsInteractor {

    private val mEncryptionUseCase: EncryptionUseCase = EncryptionUseCase()
    private val mPasswordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()

    override fun getAllPasswords(): Flowable<List<PasswordFeatureEntity>> =
        passwordsRepository.getAllPasswords()
            .flatMapSingle(this::getInvokedDecryptionUseCase)

    override fun getPasswordById(passwordId: Long): Single<PasswordFeatureEntity> =
        passwordsRepository.getPasswordById(passwordId)
            .flatMap { entity -> getInvokedDecryptionUseCase(listOf(entity)) }
            .map { it[0] }

    override fun deletePasswordById(passwordId: Long): Single<Boolean> =
        passwordsRepository.deletePasswordsByIds(listOf(passwordId))

    override fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean> =
        passwordsRepository.deletePasswordsByIds(passwordIds)

    override fun addNewPassword(passwordFeatureEntity: PasswordFeatureEntity): Single<Boolean> =
        addNewPasswords(listOf(passwordFeatureEntity))

    override fun addNewPasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Single<Boolean> {
        val domainEntitiesList = passwordFeatureEntities.mapFeatureToDomainEntitiesList()
        return getInvokedPasswordsDataCheckUseCase(domainEntitiesList)
            .flatMap { getInvokedEncryptionUseCase(domainEntitiesList) }
            .flatMap { encryptedPasswords -> passwordsRepository.addNewPasswords(encryptedPasswords) }
    }

    override fun updatePassword(passwordFeatureEntity: PasswordFeatureEntity): Single<Boolean> =
        updatePasswords(listOf(passwordFeatureEntity))

    override fun updatePasswords(passwordFeatureEntities: List<PasswordFeatureEntity>): Single<Boolean> =
        getInvokedEncryptionUseCase(passwordFeatureEntities.mapFeatureToDomainEntitiesList())
            .flatMap { encryptedEntities -> passwordsRepository.updatePasswords(encryptedEntities) }

    private fun getInvokedDecryptionUseCase(passwordRepoEntities: List<PasswordRepoEntity>) =
        Observable.fromIterable(passwordRepoEntities.mapRepoToDomainEntitiesList())
            .concatMap { entity ->
                decryptPasswordContent(entity.getPasswordContent()).map { decryptedPasswordContent ->
                    entity.passwordContentValue = decryptedPasswordContent
                    return@map entity
                }
            }
            .toList()

    private fun decryptPasswordContent(encryptedPasswordContent: String) =
        Single.create<String> { emitter ->
            mEncryptionUseCase.decrypt(
                    encryptedPasswordContent,
                    { decryptedContent -> emitter.onSuccess(decryptedContent) },
                    { throwable -> emitter.onError(throwable) }
            )
        }.toObservable()

    private fun getInvokedPasswordsDataCheckUseCase(passwordDomainEntities: List<PasswordDomainEntity>) =
        Observable.fromIterable(passwordDomainEntities)
            .concatMap { entity ->
                checkPasswordData(
                        entity.getPasswordName(),
                        entity.getPasswordContent()
                )
            }
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

    private fun getInvokedEncryptionUseCase(passwordDomainEntities: List<PasswordDomainEntity>) =
        Observable.fromIterable(passwordDomainEntities)
            .concatMap { entity ->
                encryptPasswordContent(entity.getPasswordContent()).map { encryptedPassword ->
                    entity.passwordContentValue = encryptedPassword
                    return@map entity
                }
            }
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