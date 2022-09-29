package com.ak.feature_tabpasswords_impl.screens.adapter

import android.graphics.Bitmap
import android.view.ContextMenu

data class PasswordItemModel constructor(
    val passwordId: Long,
    val name: String,
    val passwordAvatarBitmap: Bitmap?,
    val password: String,
    val isPinned: Boolean,
    val isLoadingModel: Boolean = false,
    val isItemSelected: Boolean = false,
    val isPasswordContentVisible: Boolean = false,
    val isInActionModeState: Boolean = false,
): ContextMenu.ContextMenuInfo {
    companion object {
        fun getSearchingTempModel(passwordId: Long) = getModelForId(passwordId)

        private fun getModelForId(passwordId: Long) = PasswordItemModel(
            passwordId,
            "",
            null,
            "",
            false,
        )
    }

    fun isTheSameContent(passwordItemModel: PasswordItemModel) =
        !(this.passwordId != passwordItemModel.passwordId
                || this.name != passwordItemModel.name
                || this.passwordAvatarBitmap != passwordItemModel.passwordAvatarBitmap
                || this.password != passwordItemModel.password
                || this.isPinned != passwordItemModel.isPinned
                || this.isLoadingModel != passwordItemModel.isLoadingModel
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