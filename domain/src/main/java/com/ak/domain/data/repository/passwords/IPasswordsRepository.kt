package com.ak.domain.data.repository.passwords

import com.ak.domain.data.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface IPasswordsRepository {
    fun getAllPasswords(): Flowable<List<PasswordDBEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordDBEntity>
    fun deletePasswordsByIds(passwordIds: List<Long>): Single<Boolean>
    fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean>
    fun updatePasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean>
}