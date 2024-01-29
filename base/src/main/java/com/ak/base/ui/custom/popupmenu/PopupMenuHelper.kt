package com.ak.base.ui.custom.popupmenu

import android.view.View
import com.ak.app_theme.theme.CustomTheme

class PopupMenuHelper(
    private val menuItemsGenerator: () -> List<PopupWindowMenuItem>,
) : CustomTheme.Support {

    interface Listener {
        fun onItemClicked(menuItemId: Int) {}
        fun onShow() {}
        fun onDismiss() {}
    }

    private var popupWindowMenu: PopupWindowMenu? = null
    private var listener: Listener? = null

    fun enableListener(popupMenuAnchorView: View, listener: Listener) {
        this.listener = listener
        enableListenerInternal(popupMenuAnchorView)
    }

    fun disableListener(popupMenuAnchorView: View) {
        popupMenuAnchorView.removeOnPopupWindowMenuListener()
        listener = null

        popupWindowMenu?.dismiss()
        popupWindowMenu = null
    }

    override fun applyTheme(theme: CustomTheme) {
        popupWindowMenu?.applyTheme(theme)
    }

    private fun enableListenerInternal(popupMenuAnchorView: View) {
        val safeListener = listener ?: return

        popupMenuAnchorView.setOnPopupWindowMenuListener(
            menuItemsGenerator(),
            safeListener::onItemClicked,
            { pMenu ->
                popupWindowMenu = pMenu
                safeListener.onShow()
            },
            {
                safeListener.onDismiss()
                popupWindowMenu = null
            },
        )
    }
}