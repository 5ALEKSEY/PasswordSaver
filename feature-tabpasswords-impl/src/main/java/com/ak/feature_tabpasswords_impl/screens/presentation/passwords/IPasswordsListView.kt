package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel

interface IPasswordsListView : IBaseAppView {
    fun setLoadingState(isLoading: Boolean)
    fun setEmptyPasswordsState(isEmptyViewVisible: Boolean)
    fun displayPasswords(passwordModelsList: List<PasswordItemModel>)
    fun enableToolbarScrolling()
    fun disableToolbarScrolling()
    fun showEditPasswordScreen(passwordId: Long)
}