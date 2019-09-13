package com.ak.passwordsaver.presentation.screens.passwords.logic

import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import io.reactivex.Flowable

interface PasswordsListInteractor {
    fun getAndListenAllPasswords(): Flowable<List<PasswordDBEntity>>
}