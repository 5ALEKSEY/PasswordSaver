package com.ak.core_repo_api.intefaces

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface IAccountsRepository {
    fun getAllAccounts(): Flow<List<AccountRepoEntity>>
    suspend fun getAccountById(accountId: Long): AccountRepoEntity
    suspend fun deleteAccountsByIds(accountIds: List<Long>): Boolean
    suspend fun addNewAccounts(accountRepoEntities: List<AccountRepoEntity>): Boolean
    suspend fun updateAccounts(accountRepoEntities: List<AccountRepoEntity>): Boolean
    fun clearAll(): Single<Boolean>
    fun getAccountsCount(): Int
    suspend fun pinAccount(accountIds: Long, pinnedTimestamp: Long): Boolean
    suspend fun unpinAccount(accountIds: Long): Boolean
}