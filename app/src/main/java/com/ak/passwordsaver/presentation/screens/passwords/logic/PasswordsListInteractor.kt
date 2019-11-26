package com.ak.passwordsaver.presentation.screens.passwords.logic

import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface PasswordsListInteractor {
    fun getAndListenAllPasswords(): Flowable<List<PasswordDBEntity>>
    fun getPasswordById(passwordId: Long): Single<PasswordDBEntity>
    fun deletePasswordsById(passwordIds: List<Long>): Single<Boolean>
}