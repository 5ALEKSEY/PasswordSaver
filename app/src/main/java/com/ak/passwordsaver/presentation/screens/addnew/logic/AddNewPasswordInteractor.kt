package com.ak.passwordsaver.presentation.screens.addnew.logic

import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import io.reactivex.Single

interface AddNewPasswordInteractor {
    fun addNewPassword(passwordDBEntity: PasswordDBEntity): Single<Boolean>
    fun addNewPasswords(passwordDBEntities: List<PasswordDBEntity>): Single<Boolean>
}