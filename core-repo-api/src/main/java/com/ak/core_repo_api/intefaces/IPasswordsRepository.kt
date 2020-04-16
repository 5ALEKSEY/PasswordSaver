package com.ak.core_repo_api.intefaces

import io.reactivex.Flowable
import io.reactivex.Single

interface IPasswordsRepository {
    fun getAllPasswords(): Flowable<List<PasswordRepoEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordRepoEntity>
    fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean>
    fun addNewPasswords(passwordRepoEntities: List<PasswordRepoEntity>): Single<Boolean>
    fun updatePasswords(passwordRepoEntities: List<PasswordRepoEntity>): Single<Boolean>
}