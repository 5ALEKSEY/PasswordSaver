package com.ak.tabpasswords.adapter

import android.graphics.Bitmap

data class PasswordItemModel constructor(
    val passwordId: Long,
    val name: String,
    val passwordAvatarBitmap: Bitmap?,
    val password: String,
    val isPasswordContentNeeds: Boolean,
    var isItemSelected: Boolean = false,
    var isPasswordContentVisible: Boolean = false,
    var isInActionModeState: Boolean = false
) {
    companion object {
        fun getSearchingTempModel(passwordId: Long) =
            PasswordItemModel(passwordId, "", null, "", false)
    }

    fun isTheSameContent(passwordItemModel: PasswordItemModel) =
        !(this.passwordId != passwordItemModel.passwordId
                || this.name != passwordItemModel.name
                || this.passwordAvatarBitmap != passwordItemModel.passwordAvatarBitmap
                || this.password != passwordItemModel.password
                || this.isPasswordContentNeeds != passwordItemModel.isPasswordContentNeeds
                || this.isItemSelected != passwordItemModel.isItemSelected
                || this.isPasswordContentVisible != passwordItemModel.isPasswordContentVisible
                || this.isInActionModeState != passwordItemModel.isInActionModeState)

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