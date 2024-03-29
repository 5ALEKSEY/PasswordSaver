package com.ak.feature_tabpasswords_impl.screens.adapter

interface PasswordsListClickListener {
    fun selectPasswordItem(item: PasswordItemModel)
    fun showPasswordItemContent(item: PasswordItemModel)
    fun copyPasswordItemContent(item: PasswordItemModel)
    fun editPasswordItem(item: PasswordItemModel)
    fun deletePasswordItem(item: PasswordItemModel)
    fun pinPasswordItem(item: PasswordItemModel)
    fun unpinPasswordItem(item: PasswordItemModel)
    fun onShowPopupMenu(item: PasswordItemModel)
    fun onDismissPopupmenu(item: PasswordItemModel)
}