package com.ak.feature_tabpasswords_impl.domain.usecase

data class PasswordDataCheckException(
    val emptyFields: List<Int>,
    val incorrectDataFields: List<Int>
) : Throwable() {
    companion object {
        const val PASSWORD_NAME_FIELD = 1
        const val PASSWORD_CONTENT_FIELD = 2
    }
}