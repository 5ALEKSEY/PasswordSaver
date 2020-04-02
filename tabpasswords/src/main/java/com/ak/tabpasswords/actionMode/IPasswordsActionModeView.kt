package com.ak.tabpasswords.actionMode

import com.ak.base.ui.IBaseAppView

interface IPasswordsActionModeView : IBaseAppView {
    fun showSelectedItemsQuantityText(text: String)
    fun displaySelectedMode()
    fun hideSelectedMode()
    fun showSelectStateForItem(isSelected: Boolean, passwordId: Long)
}