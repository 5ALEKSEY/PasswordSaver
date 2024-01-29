package com.ak.core_repo_impl.account

import com.ak.core_repo_api.intefaces.account.AccountRepoEntity
import com.ak.core_repo_api.intefaces.account.IAccountsRepository
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToAccountDbEntitiesList
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AccountsRepositoryImpl @Inject constructor(
    localStore: PSDatabase
) : IAccountsRepository {

    private val accountsLocalStore = localStore.getAccountsDao()

    override fun getAllAccounts(): Flow<List<AccountRepoEntity>> {
        return accountsLocalStore.getAllAccounts()
    }

    override suspend fun getAccountById(accountId: Long) = withContext(Dispatchers.IO) {
        accountsLocalStore.getAccountById(accountId)
    }

    override suspend fun deleteAccountsByIds(accountIds: List<Long>) = withContext(Dispatchers.IO) {
        val entitiesToDelete = accountIds.map { AccountDBEntity(it) }.toTypedArray()
        return@withContext accountsLocalStore.deleteAccounts(*entitiesToDelete) >= 0
    }

    override suspend fun addNewAccounts(accountRepoEntities: List<AccountRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToAdd = accountRepoEntities.mapToAccountDbEntitiesList().toTypedArray()
        return@withContext accountsLocalStore.insertNewAccount(*entitiesToAdd).isNotEmpty()
    }

    override suspend fun updateAccounts(accountRepoEntities: List<AccountRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToUpdate = accountRepoEntities.mapToAccountDbEntitiesList().toTypedArray()
        return@withContext accountsLocalStore.updateAccounts(*entitiesToUpdate) >= 0
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        accountsLocalStore.clearAccounts()
    }

    override suspend fun getAccountsCount() = withContext(Dispatchers.IO) {
        accountsLocalStore.getAccountsCount()
    }

    override suspend fun pinAccount(accountIds: Long, pinnedTimestamp: Long) = withContext(Dispatchers.IO) {
        accountsLocalStore.markAccountAsPinned(accountIds, pinnedTimestamp) >= 0
    }

    override suspend fun unpinAccount(accountIds: Long) = withContext(Dispatchers.IO) {
        accountsLocalStore.markAccountAsUnpinned(accountIds) >= 0
    }
}
