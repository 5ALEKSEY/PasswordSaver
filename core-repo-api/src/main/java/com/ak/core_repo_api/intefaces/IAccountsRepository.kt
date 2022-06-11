package com.ak.core_repo_api.intefaces

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IAccountsRepository {
    fun getAllAccounts(): Flowable<List<AccountRepoEntity>>
    fun getAccountById(accountId: Long): Single<AccountRepoEntity>
    fun deleteAccountsByIds(accountIds: List<Long>): Single<Boolean>
    fun addNewAccounts(accountRepoEntities: List<AccountRepoEntity>): Single<Boolean>
    fun updateAccounts(accountRepoEntities: List<AccountRepoEntity>): Single<Boolean>
}