package com.ak.domain.data.repository.passwords

import com.ak.domain.data.model.db.PSDatabase
import com.ak.domain.data.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    val passwordsLocalStore: PSDatabase
) : IPasswordsRepository {

    override fun getAllPasswords(): Flowable<List<PasswordDBEntity>> =
        passwordsLocalStore.getPasswordsDao()
            .getAllPasswords()
            .subscribeOn(Schedulers.io())

    override fun getPasswordById(passwordId: Long): Single<PasswordDBEntity> =
        passwordsLocalStore.getPasswordsDao()
            .getPasswordById(passwordId)
            .subscribeOn(Schedulers.io())

    override fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean> =
        Observable.fromIterable(passwordIds)
            .map { passwordId ->
                PasswordDBEntity(passwordId)
            }
            .toList()
            .flatMap {
                Single.fromCallable {
                    val deletedRows =
                        passwordsLocalStore.getPasswordsDao().deletePasswords(*it.toTypedArray())
                    return@fromCallable deletedRows > 0
                }
            }
            .subscribeOn(Schedulers.io())

    override fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean> =
        Single.fromCallable {
            passwordsLocalStore.getPasswordsDao()
                .insertNewPassword(*passwordDBEntities.toTypedArray())
        }
            .map { it.size >= 0 }
            .subscribeOn(Schedulers.io())

    override fun updatePasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean> =
        Single.fromCallable {
            passwordsLocalStore.getPasswordsDao()
                .updatePasswords(*passwordDBEntities.toTypedArray())
        }
            .map { updatedRows -> updatedRows >= 0 }
            .subscribeOn(Schedulers.io())
}