package com.ak.passwordsaver.presentation.screens.passwords.adapter

data class PasswordItemModel(
    val passwordId: Long,
    val name: String,
    val photoPath: String,
    val password: String,
    val isPasswordContentNeeds: Boolean,
    var isItemSelected: Boolean = false,
    var isPasswordContentVisible: Boolean = false
) {
    companion object {
        fun getSearchingTempModel(passwordId: Long) =
            PasswordItemModel(passwordId, "", "", "", false)
    }

    fun isTheSameContent(passwordItemModel: PasswordItemModel) =
        !(this.passwordId != passwordItemModel.passwordId
                || this.name != passwordItemModel.name
                || this.photoPath != passwordItemModel.photoPath
                || this.password != passwordItemModel.password
                || this.isPasswordContentNeeds != passwordItemModel.isPasswordContentNeeds
                || this.isItemSelected != passwordItemModel.isItemSelected
                || this.isPasswordContentVisible != passwordItemModel.isPasswordContentVisible)

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