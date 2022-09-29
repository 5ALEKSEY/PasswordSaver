package com.ak.feature_tabaccounts_impl.screens.adapter

interface AccountListClickListener {
    fun selectAccountItem(item: AccountItemModel)
    fun showAccountItemContent(item: AccountItemModel)
    fun copyAccountItemLogin(item: AccountItemModel)
    fun copyAccountItemPassword(item: AccountItemModel)
    fun editAccountItem(item: AccountItemModel)
    fun deleteAccountItem(item: AccountItemModel)
    fun pinAccount(item: AccountItemModel)
    fun unpinAccount(item: AccountItemModel)
    fun onShowPopupMenu(item: AccountItemModel)
    fun onDismissPopupmenu(item: AccountItemModel)
}