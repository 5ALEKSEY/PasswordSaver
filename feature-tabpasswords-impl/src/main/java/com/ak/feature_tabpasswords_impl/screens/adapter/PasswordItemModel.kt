package com.ak.feature_tabpasswords_impl.screens.adapter

import android.graphics.Bitmap
import androidx.annotation.IntRange

data class PasswordItemModel constructor(
    val passwordId: Long,
    val name: String,
    val passwordAvatarBitmap: Bitmap?,
    val password: String,
    val isLoadingModel: Boolean = false,
    val isItemSelected: Boolean = false,
    val isPasswordContentVisible: Boolean = false,
    val isInActionModeState: Boolean = false
) {
    companion object {
        fun getSearchingTempModel(passwordId: Long) = getModelForId(passwordId)

        fun getLoadingModels(@IntRange(from = 1) size: Int): List<PasswordItemModel> {
            val resultList = ArrayList<PasswordItemModel>(size)
            repeat(size) {
                resultList.add(
                    getModelForId(it.toLong()).copy(isLoadingModel = true)
                )
            }
            return resultList
        }

        private fun getModelForId(passwordId: Long) = PasswordItemModel(
            passwordId,
            "",
            null,
            ""
        )
    }

    fun isTheSameContent(passwordItemModel: PasswordItemModel) =
        !(this.passwordId != passwordItemModel.passwordId
                || this.name != passwordItemModel.name
                || this.passwordAvatarBitmap != passwordItemModel.passwordAvatarBitmap
                || this.password != passwordItemModel.password
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