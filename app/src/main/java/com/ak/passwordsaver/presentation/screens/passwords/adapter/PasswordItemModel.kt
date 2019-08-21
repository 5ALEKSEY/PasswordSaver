package com.ak.passwordsaver.presentation.screens.passwords.adapter

data class PasswordItemModel(
    val passwordId: Long,
    val name: String,
    val photoUrl: String,
    val password: String,
    val isPasswordContentNeeds: Boolean,
    var isPasswordContentVisible: Boolean = false
) {
    companion object {
        fun getSearchingTempModel(passwordId: Long) =
            PasswordItemModel(passwordId, "", "", "", false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordItemModel

        if (passwordId != other.passwordId) return false

        return true
    }

    override fun hashCode(): Int {
        return passwordId.hashCode()
    }
}