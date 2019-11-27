package com.ak.passwordsaver.presentation.screens.passwordmanage.logic

import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import io.reactivex.Single

interface IAddNewPasswordInteractor {
    fun addNewPassword(passwordDBEntity: PasswordDBEntity): Single<Boolean>
    fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean>
}