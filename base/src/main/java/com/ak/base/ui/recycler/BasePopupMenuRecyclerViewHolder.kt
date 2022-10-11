package com.ak.base.ui.recycler

import android.view.View
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.ui.custom.popupmenu.PopupMenuHelper
import com.ak.base.ui.custom.popupmenu.PopupWindowMenuItem

abstract class BasePopupMenuRecyclerViewHolder(
    itemView: View,
): CustomThemeRecyclerViewHolder(itemView) {

    protected abstract val popupMenuListener: PopupMenuHelper.Listener

    private val popupMenuHelper = PopupMenuHelper(::generateMenuItems)

    protected abstract fun generateMenuItems(): List<PopupWindowMenuItem>

    override fun applyTheme(theme: CustomTheme) {
        popupMenuHelper.applyTheme(theme)
    }

    protected fun enablePopupMenuListener() {
        popupMenuHelper.enableListener(itemView, popupMenuListener)
    }

    protected fun disablePopupMenuListener() {
        popupMenuHelper.disableListener(itemView)
    }
}