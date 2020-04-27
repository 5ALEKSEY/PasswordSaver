package com.ak.feature_tabaccounts_impl.screens.actionMode

import com.ak.base.ui.IBaseAppView

interface IAccountsActionModeView : IBaseAppView {
    fun showSelectedItemsQuantityText(text: String)
    fun displaySelectedMode()
    fun hideSelectedMode()
    fun showSelectStateForItem(isSelected: Boolean, accountId: Long)
}