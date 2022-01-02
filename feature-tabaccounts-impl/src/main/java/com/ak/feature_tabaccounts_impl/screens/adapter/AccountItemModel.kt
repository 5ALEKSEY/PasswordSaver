package com.ak.feature_tabaccounts_impl.screens.adapter

data class AccountItemModel constructor(
    val accountId: Long,
    val name: String,
    val login: String,
    val password: String,
    val isItemSelected: Boolean = false,
    val isInActionModeState: Boolean = false,
    val isAccountContentVisible: Boolean = false
) {
    companion object {
        fun getSearchingTempModel(accountId: Long) = AccountItemModel(accountId, "", "", "")
    }

    fun isTheSameContent(accountItemModel: AccountItemModel) =
        !(this.accountId != accountItemModel.accountId
                || this.name != accountItemModel.name
                || this.login != accountItemModel.login
                || this.password != accountItemModel.password
                || this.isItemSelected != accountItemModel.isItemSelected
                || this.isInActionModeState != accountItemModel.isInActionModeState
                || this.isAccountContentVisible != accountItemModel.isAccountContentVisible)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountItemModel

        if (accountId != other.accountId) return false

        return true
    }

    override fun hashCode(): Int {
        return accountId.hashCode()
    }
}