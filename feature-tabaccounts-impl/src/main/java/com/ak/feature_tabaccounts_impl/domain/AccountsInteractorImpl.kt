package com.ak.feature_tabaccounts_impl.domain

import com.ak.base.extensions.onErrorSafe
import com.ak.base.extensions.onSuccessSafe
import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.domain.entity.mapFeatureToDomainEntitiesList
import com.ak.feature_tabaccounts_impl.domain.entity.mapRepoToDomainEntitiesList
import com.ak.feature_tabaccounts_impl.domain.usecase.AccountDataCheckUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AccountsInteractorImpl @Inject constructor(
    private val accountsRepository: IAccountsRepository,
    private val encryptionManager: IEncryptionManager
) : IAccountsInteractor {

    private val accountDataCheckUseCase: AccountDataCheckUseCase = AccountDataCheckUseCase()

    override fun getAllAccounts(): Flowable<List<AccountFeatureEntity>> =
        accountsRepository.getAllAccounts()
            .flatMapSingle(this::getInvokedDecryptionUseCase)

    override fun getAccountById(accountId: Long): Single<AccountFeatureEntity> =
        accountsRepository.getAccountById(accountId)
            .flatMap { entity -> getInvokedDecryptionUseCase(listOf(entity)) }
            .map { it[0] }

    override fun deleteAccountById(accountId: Long): Single<Boolean> =
        deleteAccountsByIds(listOf(accountId))

    override fun deleteAccountsByIds(accountIds: List<Long>): Single<Boolean> =
        accountsRepository.deleteAccountsByIds(accountIds)

    override fun addNewAccount(accountFeatureEntity: AccountFeatureEntity): Single<Boolean> =
        addNewAccounts(listOf(accountFeatureEntity))

    override fun addNewAccounts(accountFeatureEntities: List<AccountFeatureEntity>): Single<Boolean> {
        val domainEntitiesList = accountFeatureEntities.mapFeatureToDomainEntitiesList()
        return getInvokedAccountsDataCheckUseCase(domainEntitiesList)
            .flatMap { getInvokedEncryptionUseCase(domainEntitiesList) }
            .flatMap { encryptedAccounts -> accountsRepository.addNewAccounts(encryptedAccounts) }
    }

    override fun updateAccount(accountFeatureEntity: AccountFeatureEntity): Single<Boolean> =
        updateAccounts(listOf(accountFeatureEntity))

    override fun updateAccounts(accountFeatureEntities: List<AccountFeatureEntity>): Single<Boolean> =
        getInvokedEncryptionUseCase(accountFeatureEntities.mapFeatureToDomainEntitiesList())
            .flatMap { encryptedEntities -> accountsRepository.updateAccounts(encryptedEntities) }

    override fun pinAccount(accountId: Long, pinnedTimestamp: Long): Single<Boolean> {
        return accountsRepository.pinAccount(accountId, pinnedTimestamp)
    }

    override fun unpinAccount(accountId: Long): Single<Boolean> {
        return accountsRepository.unpinAccount(accountId)
    }

    private fun getInvokedDecryptionUseCase(accountRepoEntities: List<AccountRepoEntity>) =
        Observable.fromIterable(accountRepoEntities.mapRepoToDomainEntitiesList())
            .concatMap { entity ->
                // decrypt account login
                decryptAccountData(entity.getAccountLogin()).map { decryptedAccountLogin ->
                    entity.accountLoginValue = decryptedAccountLogin
                    return@map entity
                }
            }
            .concatMap { entity ->
                // decrypt account password
                decryptAccountData(entity.getAccountPassword()).map { decryptedAccountPassword ->
                    entity.accountPasswordValue = decryptedAccountPassword
                    return@map entity
                }
            }
            .toList()

    private fun decryptAccountData(encryptedData: String) =
        Single.create<String> { emitter ->
            encryptionManager.decrypt(
                    encryptedData,
                    { decryptedContent -> emitter.onSuccessSafe(decryptedContent) },
                    { throwable -> emitter.onErrorSafe(throwable) }
            )
        }.toObservable()

    private fun getInvokedAccountsDataCheckUseCase(accountDomainEntities: List<AccountDomainEntity>) =
        Observable.fromIterable(accountDomainEntities)
            .concatMap { entity ->
                checkAccountData(
                        entity.getAccountName(),
                        entity.getAccountLogin(),
                        entity.getAccountPassword()
                )
            }
            .singleOrError()

    private fun checkAccountData(accountName: String, accountLogin: String, accountPassword: String) =
        Single.create<Boolean> { emitter ->
            try {
                accountDataCheckUseCase.verifyAccountData(accountName, accountLogin, accountPassword)
                emitter.onSuccessSafe(true)
            } catch (e: Exception) {
                emitter.onErrorSafe(e)
            }
        }.toObservable()

    private fun getInvokedEncryptionUseCase(accountDomainEntities: List<AccountDomainEntity>) =
        Observable.fromIterable(accountDomainEntities)
            .concatMap { entity ->
                // encrypt account login
                encryptAccountData(entity.getAccountLogin()).map { encryptedLogin ->
                    entity.accountLoginValue = encryptedLogin
                    return@map entity
                }
            }
            .concatMap { entity ->
                // encrypt account password
                encryptAccountData(entity.getAccountPassword()).map { encryptedPassword ->
                    entity.accountPasswordValue = encryptedPassword
                    return@map entity
                }
            }
            .toList()

    private fun encryptAccountData(dataForEncrypt: String) =
        Single.create<String> { emitter ->
            encryptionManager.encrypt(
                    dataForEncrypt,
                    { encryptedContent -> emitter.onSuccessSafe(encryptedContent) },
                    { throwable -> emitter.onErrorSafe(throwable) }
            )
        }.toObservable()
}