package com.ak.base.ui.custom.popupmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.R

typealias PopupWindowMenuItemClickListener = (id: Int) -> Unit

class PopupWindowMenuAdapter(
    private val menuItems: List<PopupWindowMenuItem>,
    private val menuItemClickListener: PopupWindowMenuItemClickListener,
) : CustomThemeRecyclerViewAdapter<PopupWindowMenuAdapter.PopupWindowMenuItemViewHolder>() {

    inner class PopupWindowMenuItemViewHolder(itemView: View) : CustomThemeRecyclerViewHolder(itemView) {
        private val menuItemImage by lazy { itemView.findViewById<ImageView>(R.id.ivPopupWindowMenuItemImage) }
        private val menuItemTitle by lazy { itemView.findViewById<TextView>(R.id.tvPopupWindowMenuItemTitle) }

        private var menuItem: PopupWindowMenuItem? = null

        override fun applyTheme(theme: CustomTheme) {
        }

        fun bindMenuItem(menuItem: PopupWindowMenuItem) {
            this.menuItem = menuItem

            itemView.setOnClickListener { menuItemClickListener(menuItem.id) }

            menuItem.image.drawable?.let { menuItemImage.setImageDrawable(it) }
            menuItem.image.drawableRes?.let { menuItemImage.setImageResource(it) }

            menuItem.title.content?.let { menuItemTitle.text = it }
            menuItem.title.contentRes?.let { menuItemTitle.text = itemView.resources.getText(it) }

            setMenuItemTheme(CustomThemeManager.getCurrentAppliedTheme())
        }

        private fun setMenuItemTheme(theme: CustomTheme) {
            fun setTitleColor(@AttrRes textColorAttrRes: Int) {
                CustomThemeApplier.applyTextColor(theme, menuItemTitle, textColorAttrRes)
            }

            fun setIconTint(@AttrRes iconTintAttrRes: Int) {
                CustomThemeApplier.applyTint(theme, menuItemImage, iconTintAttrRes)
            }

            setTitleColor(menuItem?.customItemTint?.titleTintAttrRes ?: R.attr.themedPrimaryTextColor)
            setIconTint(menuItem?.customItemTint?.iconTintAttrRes ?: R.attr.themedPrimaryTextColor)
        }
    }

    override fun onBindViewHolder(theme: CustomTheme, viewHolder: PopupWindowMenuItemViewHolder, position: Int) {
        viewHolder.bindMenuItem(menuItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupWindowMenuItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_popup_window_menu_item, parent, false)
        return PopupWindowMenuItemViewHolder(view)
    }

    override fun getItemCount() = menuItems.size
}