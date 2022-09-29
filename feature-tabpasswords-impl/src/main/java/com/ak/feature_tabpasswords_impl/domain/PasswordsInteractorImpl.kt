package com.ak.feature_tabpasswords_impl.domain

import com.ak.base.extensions.onErrorSafe
import com.ak.base.extensions.onSuccessSafe
import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.domain.entity.PasswordDomainEntity
import com.ak.feature_tabpasswords_impl.domain.entity.mapFeatureToDomainEntitiesList
import com.ak.feature_tabpasswords_impl.domain.entity.mapRepoToDomainEntitiesList
import com.ak.feature_tabpasswords_impl.domain.usecase.PasswordDataCheckUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PasswordsInteractorImpl @Inject constructor(
    private val passwordsRepository: IPasswordsRepository,
    private val encryptionManager: IEncryptionManager
) : IPasswordsInteractor {

    private val passwordDataCheckUseCase: PasswordDataCheckUseCase = PasswordDataCheckUseCase()

    override fun getAllPasswords(): Flowable<List<PasswordFeatureEntity>> =
        passwordsRepository.getAllPasswords()
            .flatMapSingle(this::getInvokedDecryptionUseCase)

    override fun getPasswordById(passwordId: Long): Single<PasswordFeatureEntity> =
        passwordsRepository.getPasswordById(passwordId)
            .flatMap { entity -> getInvokedDecryptionUseCase(listOf(entity)) }
            .map { it[0] }

    override fun deletePasswordById(passwordId: Long): Single<Boolean> =
        deletePasswordsByIds(listOf(passwordId))

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

    override fun pinPassword(passwordId: Long, pinnedTimestamp: Long): Single<Boolean> {
        return passwordsRepository.pinPassword(passwordId, pinnedTimestamp)
    }

    override fun unpinPassword(passwordId: Long): Single<Boolean> {
        return passwordsRepository.unpinPassword(passwordId)
    }

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
            encryptionManager.decrypt(
                    encryptedPasswordContent,
                    { decryptedContent -> emitter.onSuccessSafe(decryptedContent) },
                    { throwable -> emitter.onErrorSafe(throwable) }
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
                passwordDataCheckUseCase.verifyPasswordData(passwordName, passwordContent)
                emitter.onSuccessSafe(true)
            } catch (e: Exception) {
                emitter.onErrorSafe(e)
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
            encryptionManager.encrypt(
                    passwordContent,
                    { encryptedContent -> emitter.onSuccessSafe(encryptedContent) },
                    { throwable -> emitter.onErrorSafe(throwable) }
            )
        }.toObservable()
}