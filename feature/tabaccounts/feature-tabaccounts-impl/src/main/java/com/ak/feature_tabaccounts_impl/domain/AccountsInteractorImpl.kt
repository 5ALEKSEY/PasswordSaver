package com.ak.feature_tabaccounts_impl.domain

import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.feature_encryption_api.interfaces.IEncryptionManager
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.domain.entity.AccountDomainEntity
import com.ak.feature_tabaccounts_impl.domain.entity.mapFeatureToDomainEntitiesList
import com.ak.feature_tabaccounts_impl.domain.entity.mapRepoToDomainEntitiesList
import com.ak.feature_tabaccounts_impl.domain.entity.mapToDomainEntity
import com.ak.feature_tabaccounts_impl.domain.usecase.AccountDataCheckUseCase
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AccountsInteractorImpl @Inject constructor(
    private val accountsRepository: IAccountsRepository,
    private val encryptionManager: IEncryptionManager
) : IAccountsInteractor {

    private val accountDataCheckUseCase: AccountDataCheckUseCase = AccountDataCheckUseCase()

    override fun getAllAccounts(): Flow<List<AccountFeatureEntity>> {
        return accountsRepository.getAllAccounts().map {
            decryptAccountEntities(it.mapRepoToDomainEntitiesList())
        }
    }

    override suspend fun getAccountById(accountId: Long) = withContext(Dispatchers.IO) {
        accountsRepository.getAccountById(accountId)
            .mapToDomainEntity()
            .apply {
                accountPasswordValue = encryptionManager.decrypt(accountPasswordValue)
                accountLoginValue = encryptionManager.decrypt(accountLoginValue)
            }
    }

    override suspend fun deleteAccountById(accountId: Long) = withContext(Dispatchers.IO) {
        deleteAccountsByIds(listOf(accountId))
    }

    override suspend fun deleteAccountsByIds(accountIds: List<Long>) = withContext(Dispatchers.IO) {
        accountsRepository.deleteAccountsByIds(accountIds)
    }

    override suspend fun addNewAccount(accountFeatureEntity: AccountFeatureEntity) = withContext(Dispatchers.IO) {
        addNewAccounts(listOf(accountFeatureEntity))
    }

    override suspend fun addNewAccounts(accountFeatureEntities: List<AccountFeatureEntity>) = withContext(Dispatchers.IO) {
        val domainEntities = accountFeatureEntities.mapFeatureToDomainEntitiesList()

        // verify account entities
        domainEntities.forEach {
            accountDataCheckUseCase.verifyAccountData(
                accountName = it.accountNameValue,
                accountLogin = it.accountLoginValue,
                accountPassword = it.accountPasswordValue,
            )
        }

        val domainEntitiesEncrypted = encryptAccountEntities(domainEntities)
        return@withContext accountsRepository.addNewAccounts(domainEntitiesEncrypted)
    }

    override suspend fun updateAccount(accountFeatureEntity: AccountFeatureEntity) = withContext(Dispatchers.IO) {
        updateAccounts(listOf(accountFeatureEntity))
    }

    override suspend fun updateAccounts(accountFeatureEntities: List<AccountFeatureEntity>) = withContext(Dispatchers.IO) {
        val domainEntities = accountFeatureEntities.mapFeatureToDomainEntitiesList()

        // verify account entities
        domainEntities.forEach {
            accountDataCheckUseCase.verifyAccountData(
                accountName = it.accountNameValue,
                accountLogin = it.accountLoginValue,
                accountPassword = it.accountPasswordValue,
            )
        }

        val domainEntitiesEncrypted = encryptAccountEntities(domainEntities)
        return@withContext accountsRepository.updateAccounts(domainEntitiesEncrypted)
    }

    override suspend fun pinAccount(accountId: Long, pinnedTimestamp: Long) = withContext(Dispatchers.IO) {
        accountsRepository.pinAccount(accountId, pinnedTimestamp)
    }

    override suspend fun unpinAccount(accountId: Long) = withContext(Dispatchers.IO) {
        accountsRepository.unpinAccount(accountId)
    }

    private suspend fun decryptAccountEntities(
        entities: List<AccountDomainEntity>,
    ) = withContext(Dispatchers.IO) {
        entities.mapToDecryptionCoroutines().awaitAll()
    }

    private suspend fun encryptAccountEntities(
        entities: List<AccountDomainEntity>,
    ) = withContext(Dispatchers.IO) {
        entities.mapToEncryptionCoroutines().awaitAll()
    }

    private suspend fun List<AccountDomainEntity>.mapToDecryptionCoroutines() = withContext(Dispatchers.IO) {
        map {
            async {
                it.apply {
                    accountPasswordValue = encryptionManager.decrypt(accountPasswordValue)
                    accountLoginValue = encryptionManager.decrypt(accountLoginValue)
                }
            }
        }
    }

    private suspend fun List<AccountDomainEntity>.mapToEncryptionCoroutines() = withContext(Dispatchers.IO) {
        map {
            async {
                it.apply {
                    accountPasswordValue = encryptionManager.encrypt(accountPasswordValue)
                    accountLoginValue = encryptionManager.encrypt(accountLoginValue)
                }
            }
        }
    }
}