package com.ak.passwordsaver.presentation.screens.passwordmanage.logic.usecases

data class PasswordDataCheckException(
    val emptyFields: List<Int>,
    val incorrectDataFields: List<Int>
) : Throwable() {
    companion object {
        const val PASSWORD_NAME_FIELD = 1
        const val PASSWORD_CONTENT_FIELD = 2
    }
}