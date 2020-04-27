package com.ak.core_repo_api.intefaces

import io.reactivex.Flowable
import io.reactivex.Single

interface IAccountsRepository {
    fun getAllAccounts(): Flowable<List<AccountRepoEntity>>
    fun getAccountById(accountId: Long): Single<AccountRepoEntity>
    fun deleteAccountsByIds(accountIds: List<Long>): Single<Boolean>
    fun addNewAccounts(accountRepoEntities: List<AccountRepoEntity>): Single<Boolean>
    fun updateAccounts(accountRepoEntities: List<AccountRepoEntity>): Single<Boolean>
}