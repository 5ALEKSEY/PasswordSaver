package com.ak.core_repo_impl

import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToAccountDbEntitiesList
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AccountsRepositoryImpl @Inject constructor(
    private val accountsLocalStore: PSDatabase
) : IAccountsRepository {

    override fun getAllAccounts(): Flow<List<AccountRepoEntity>> {
        return accountsLocalStore.getAccountsDao().getAllAccounts()
    }

    override suspend fun getAccountById(accountId: Long) = withContext(Dispatchers.IO) {
        accountsLocalStore.getAccountsDao().getAccountById(accountId)
    }

    override suspend fun deleteAccountsByIds(accountIds: List<Long>) = withContext(Dispatchers.IO) {
        val entitiesToDelete = accountIds.map { AccountDBEntity(it) }.toTypedArray()
        return@withContext accountsLocalStore.getAccountsDao().deleteAccounts(*entitiesToDelete) >= 0
    }

    override suspend fun addNewAccounts(accountRepoEntities: List<AccountRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToAdd = accountRepoEntities.mapToAccountDbEntitiesList().toTypedArray()
        return@withContext accountsLocalStore.getAccountsDao().insertNewAccount(*entitiesToAdd).isNotEmpty()
    }

    override suspend fun updateAccounts(accountRepoEntities: List<AccountRepoEntity>) = withContext(Dispatchers.IO) {
        val entitiesToUpdate = accountRepoEntities.mapToAccountDbEntitiesList().toTypedArray()
        return@withContext accountsLocalStore.getAccountsDao().updateAccounts(*entitiesToUpdate) >= 0
    }

    override fun clearAll(): Single<Boolean> =
        Single.fromCallable {
            accountsLocalStore.getAccountsDao().clearAccounts()
            return@fromCallable true
        }.subscribeOn(Schedulers.io())

    override fun getAccountsCount() = accountsLocalStore.getAccountsDao().getAccountsCount()

    override suspend fun pinAccount(accountIds: Long, pinnedTimestamp: Long) = withContext(Dispatchers.IO) {
        accountsLocalStore.getAccountsDao().markAccountAsPinned(accountIds, pinnedTimestamp) >= 0
    }

    override suspend fun unpinAccount(accountIds: Long) = withContext(Dispatchers.IO) {
        accountsLocalStore.getAccountsDao().markAccountAsUnpinned(accountIds) >= 0
    }
}
