package com.ak.core_repo_api.intefaces

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IPasswordsRepository {
    fun getAllPasswords(): Flowable<List<PasswordRepoEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordRepoEntity>
    fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean>
    fun addNewPasswords(passwordRepoEntities: List<PasswordRepoEntity>): Single<Boolean>
    fun updatePasswords(passwordRepoEntities: List<PasswordRepoEntity>): Single<Boolean>
    fun clearAll(): Single<Boolean>
}