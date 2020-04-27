package com.ak.feature_tabaccounts_impl.domain.usecase

class AccountDataCheckUseCase {

    @Throws(AccountDataCheckException::class)
    fun verifyAccountData(accountName: String, accountLogin: String, accountPassword: String) {
        val emptyFields = arrayListOf<Int>()
        val incorrectFields = arrayListOf<Int>()
        var isVerifySuccess = true

        if (accountName.isEmpty()) {
            emptyFields.add(AccountDataCheckException.ACCOUNT_NAME_FIELD)
            isVerifySuccess = false
        }

        if (accountLogin.isEmpty()) {
            emptyFields.add(AccountDataCheckException.ACCOUNT_LOGIN_FIELD)
            isVerifySuccess = false
        }

        if (accountPassword.isEmpty()) {
            emptyFields.add(AccountDataCheckException.ACCOUNT_PASSWORD_FIELD)
            isVerifySuccess = false
        }

        // TODO: verify incorrect data

        if (!isVerifySuccess) throw AccountDataCheckException(
            emptyFields,
            incorrectFields
        )
    }
}