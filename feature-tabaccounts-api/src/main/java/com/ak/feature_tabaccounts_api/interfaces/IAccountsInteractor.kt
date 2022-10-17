package com.ak.feature_tabaccounts_api.interfaces

import kotlinx.coroutines.flow.Flow

interface IAccountsInteractor {
    fun getAllAccounts(): Flow<List<AccountFeatureEntity>>
    suspend fun getAccountById(accountId: Long): AccountFeatureEntity
    suspend fun deleteAccountById(accountId: Long): Boolean
    suspend fun deleteAccountsByIds(accountIds: List<Long>): Boolean
    suspend fun addNewAccount(accountFeatureEntity: AccountFeatureEntity): Boolean
    suspend fun addNewAccounts(accountFeatureEntities: List<AccountFeatureEntity>): Boolean
    suspend fun updateAccount(accountFeatureEntity: AccountFeatureEntity): Boolean
    suspend fun updateAccounts(accountFeatureEntities: List<AccountFeatureEntity>): Boolean
    suspend fun pinAccount(accountId: Long, pinnedTimestamp: Long): Boolean
    suspend fun unpinAccount(accountId: Long): Boolean
}