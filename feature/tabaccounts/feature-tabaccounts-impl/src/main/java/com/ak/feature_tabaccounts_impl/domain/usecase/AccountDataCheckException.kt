package com.ak.feature_tabaccounts_impl.domain.usecase

data class AccountDataCheckException(
    val emptyFields: List<Int>,
    val incorrectDataFields: List<Int>
) : Throwable() {
    companion object {
        const val ACCOUNT_NAME_FIELD = 1
        const val ACCOUNT_LOGIN_FIELD = 2
        const val ACCOUNT_PASSWORD_FIELD = 3
    }
}