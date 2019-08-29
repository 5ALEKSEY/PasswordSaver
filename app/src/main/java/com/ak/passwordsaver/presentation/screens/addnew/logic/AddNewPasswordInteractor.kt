package com.ak.passwordsaver.presentation.screens.addnew.logic

import io.reactivex.Single

interface AddNewPasswordInteractor {
    fun addNewPassword(passwordName: String, passwordContent: String): Single<Boolean>
}