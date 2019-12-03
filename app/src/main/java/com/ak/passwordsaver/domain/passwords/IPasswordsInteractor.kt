package com.ak.passwordsaver.domain.passwords

import com.ak.passwordsaver.data.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface IPasswordsInteractor {
    // TODO: refactor with domain models
    fun getAllPasswords(): Flowable<List<PasswordDBEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordDBEntity>
    fun deletePasswordById(passwordId: Long): Single<Boolean>
    fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean>
    fun addNewPassword(passwordDBEntity: PasswordDBEntity): Single<Boolean>
    fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean>
    fun updatePassword(passwordDBEntity: PasswordDBEntity): Single<Boolean>
    fun updatePasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean>
}