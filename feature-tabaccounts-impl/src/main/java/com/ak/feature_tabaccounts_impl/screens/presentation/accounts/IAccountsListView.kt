package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel

interface IAccountsListView : IBaseAppView {
    fun setLoadingState(isLoading: Boolean)
    fun setEmptyAccountsState(isEmptyViewVisible: Boolean)
    fun setAccountContentVisibility(accountId: Long, contentVisibilityState: Boolean)
    fun displayAccounts(accountModelsList: List<AccountItemModel>)
    fun enableToolbarScrolling()
    fun disableToolbarScrolling()
    fun showAccountActionsDialog()
    fun hideAccountActionsDialog()
    fun showEditAccountScreen(accountId: Long)
}