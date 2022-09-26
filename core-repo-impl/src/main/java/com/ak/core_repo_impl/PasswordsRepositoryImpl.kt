package com.ak.core_repo_impl

import com.ak.core_repo_api.intefaces.IPasswordsRepository
import com.ak.core_repo_api.intefaces.PasswordRepoEntity
import com.ak.core_repo_impl.data.model.db.PSDatabase
import com.ak.core_repo_impl.data.model.db.entities.PasswordDBEntity
import com.ak.core_repo_impl.data.model.mapper.mapToPasswordDbEntitiesList
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    private val passwordsLocalStore: PSDatabase
) : IPasswordsRepository {

    override fun getAllPasswords(): Flowable<List<PasswordRepoEntity>> =
        passwordsLocalStore.getPasswordsDao()
            .getAllPasswords()
            .subscribeOn(Schedulers.io())
            .map { it }

    override fun getPasswordById(passwordId: Long): Single<PasswordRepoEntity> =
        passwordsLocalStore.getPasswordsDao().getPasswordById(passwordId).subscribeOn(Schedulers.io()).cast(PasswordRepoEntity::class.java)

    override fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean> =
        Observable.fromIterable(passwordIds)
            .map { passwordId ->
                PasswordDBEntity(passwordId)
            }
            .toList()
            .flatMap {
                Single.fromCallable {
                    val deletedRows = passwordsLocalStore.getPasswordsDao().deletePasswords(*it.toTypedArray())
                    return@fromCallable deletedRows > 0
                }
            }
            .subscribeOn(Schedulers.io())

    override fun addNewPasswords(passwordRepoEntities: List<PasswordRepoEntity>): Single<Boolean> =
        Single.fromCallable {
            passwordsLocalStore.getPasswordsDao().insertNewPassword(*passwordRepoEntities.mapToPasswordDbEntitiesList().toTypedArray())
        }
            .map { it.size >= 0 }
            .subscribeOn(Schedulers.io())

    override fun updatePasswords(passwordRepoEntities: List<PasswordRepoEntity>): Single<Boolean> =
        Single.fromCallable {
            passwordsLocalStore.getPasswordsDao().updatePasswords(*passwordRepoEntities.mapToPasswordDbEntitiesList().toTypedArray())
        }
            .map { updatedRows -> updatedRows >= 0 }
            .subscribeOn(Schedulers.io())

    override fun clearAll(): Single<Boolean> =
        Single.fromCallable {
            passwordsLocalStore.getPasswordsDao().clearPasswords()
            return@fromCallable true
        }.subscribeOn(Schedulers.io())

    override fun getPasswordsCount() = passwordsLocalStore.getPasswordsDao().getPasswordsCount()
}