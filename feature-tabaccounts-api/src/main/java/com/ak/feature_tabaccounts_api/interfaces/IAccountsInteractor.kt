package com.ak.feature_tabaccounts_api.interfaces

import io.reactivex.Flowable
import io.reactivex.Single

interface IAccountsInteractor {
    fun getAllAccounts(): Flowable<List<AccountFeatureEntity>>
    fun getAccountById(accountId: Long): Single<AccountFeatureEntity>
    fun deleteAccountById(accountId: Long): Single<Boolean>
    fun deleteAccountsByIds(accountIds: List<Long>): Single<Boolean>
    fun addNewAccount(accountFeatureEntity: AccountFeatureEntity): Single<Boolean>
    fun addNewAccounts(accountFeatureEntities: List<AccountFeatureEntity>): Single<Boolean>
    fun updateAccount(accountFeatureEntity: AccountFeatureEntity): Single<Boolean>
    fun updateAccounts(accountFeatureEntities: List<AccountFeatureEntity>): Single<Boolean>
}