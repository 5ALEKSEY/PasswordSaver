package com.ak.base.ui.custom.popupmenu

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.ComplexViewsApplier
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.base.R

class PopupWindowMenu(contentView: View) : PopupWindow(
    contentView,
    ConstraintLayout.LayoutParams.WRAP_CONTENT,
    ConstraintLayout.LayoutParams.WRAP_CONTENT,
    true,
), CustomTheme.Support {

    init {
        animationStyle = R.style.popup_animation_quick
    }

    private var menuRecycler: RecyclerView? = null

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyBackgroundTint(theme, menuRecycler, R.attr.themedSecondaryBackgroundColor)
        ComplexViewsApplier.applyForRecyclerView(menuRecycler, theme)
    }

    fun show(
        menuItems: List<PopupWindowMenuItem>,
        popupMenuItemClickListener: PopupWindowMenuItemClickListener,
        clickedView: View,
        clickedX: Float,
        clickedY: Float,
    ) {
        initMenuItemsList(menuItems, popupMenuItemClickListener)
        val xOffset = (clickedView.x + clickedX).toInt()
        val yOffset = (clickedView.y + clickedY).toInt()
        showAtLocation(clickedView, Gravity.NO_GRAVITY, xOffset, yOffset)
    }

    private fun initMenuItemsList(
        menuItems: List<PopupWindowMenuItem>,
        popupMenuItemClickListener: PopupWindowMenuItemClickListener,
    ) {
        menuRecycler = contentView.findViewById<RecyclerView>(R.id.rvPopupWindowMenuList).apply {
            val menuItemClickListenerProxy = { id: Int ->
                popupMenuItemClickListener(id)
                dismiss()
            }
            adapter = PopupWindowMenuAdapter(menuItems, menuItemClickListenerProxy)
        }
    }

    companion object {
        fun create(themedContext: Context) : PopupWindowMenu {
            val contentView = LayoutInflater
                .from(themedContext)
                .inflate(R.layout.layout_popup_window_menu, null)
            return PopupWindowMenu(contentView)
        }
    }
}