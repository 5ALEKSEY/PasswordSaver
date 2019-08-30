package com.ak.passwordsaver.presentation.screens.passwords.actionMode

import com.ak.passwordsaver.presentation.base.IBaseAppView

interface IPasswordsActionModeView : IBaseAppView {
    fun showSelectedItemsQuantityText(text: String)
    fun displaySelectedMode()
    fun hideSelectedMode()
    fun showSelectStateForItem(isSelected: Boolean, passwordId: Long)
}