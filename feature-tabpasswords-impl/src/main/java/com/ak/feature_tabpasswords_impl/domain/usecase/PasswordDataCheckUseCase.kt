package com.ak.feature_tabpasswords_impl.domain.usecase

class PasswordDataCheckUseCase {

    @Throws(PasswordDataCheckException::class)
    fun verifyPasswordData(passwordName: String, passwordContent: String) {
        val emptyFields = arrayListOf<Int>()
        val incorrectFields = arrayListOf<Int>()
        var isVerifySuccess = true

        if (passwordName.isEmpty()) {
            emptyFields.add(PasswordDataCheckException.PASSWORD_NAME_FIELD)
            isVerifySuccess = false
        }

        if (passwordContent.isEmpty()) {
            emptyFields.add(PasswordDataCheckException.PASSWORD_CONTENT_FIELD)
            isVerifySuccess = false
        }

        // TODO: verify incorrect data

        if (!isVerifySuccess) throw PasswordDataCheckException(
            emptyFields,
            incorrectFields
        )
    }
}