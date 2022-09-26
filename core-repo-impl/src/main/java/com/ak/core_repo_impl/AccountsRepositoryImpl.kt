package com.ak.core_repo_impl

import com.ak.core_repo_api.intefaces.AccountRepoEntity
import com.ak.core_repo_api.intefaces.IAccountsRepository
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.AccountDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToAccountDbEntitiesList
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val accountsLocalStore: PSDatabase
) : IAccountsRepository {

    override fun getAllAccounts(): Flowable<List<AccountRepoEntity>> =
        accountsLocalStore.getAccountsDao()
            .getAllAccounts()
            .subscribeOn(Schedulers.io())
            .map { it }

    override fun getAccountById(accountId: Long): Single<AccountRepoEntity> =
        accountsLocalStore.getAccountsDao().getAccountById(accountId).subscribeOn(Schedulers.io()).cast(AccountRepoEntity::class.java)

    override fun deleteAccountsByIds(accountIds: List<Long>): Single<Boolean> =
        Observable.fromIterable(accountIds)
            .map { accountId ->
                AccountDBEntity(accountId)
            }
            .toList()
            .flatMap {
                Single.fromCallable {
                    val deletedRows = accountsLocalStore.getAccountsDao().deleteAccounts(*it.toTypedArray())
                    return@fromCallable deletedRows > 0
                }
            }
            .subscribeOn(Schedulers.io())

    override fun addNewAccounts(accountRepoEntities: List<AccountRepoEntity>): Single<Boolean> =
        Single.fromCallable {
            accountsLocalStore.getAccountsDao().insertNewAccount(*accountRepoEntities.mapToAccountDbEntitiesList().toTypedArray())
        }
            .map { it.size >= 0 }
            .subscribeOn(Schedulers.io())

    override fun updateAccounts(accountRepoEntities: List<AccountRepoEntity>): Single<Boolean> =
        Single.fromCallable {
            accountsLocalStore.getAccountsDao().updateAccounts(*accountRepoEntities.mapToAccountDbEntitiesList().toTypedArray())
        }
            .map { updatedRows -> updatedRows >= 0 }
            .subscribeOn(Schedulers.io())

    override fun clearAll(): Single<Boolean> =
        Single.fromCallable {
            accountsLocalStore.getAccountsDao().clearAccounts()
            return@fromCallable true
        }.subscribeOn(Schedulers.io())

    override fun getAccountsCount() = accountsLocalStore.getAccountsDao().getAccountsCount()
}
