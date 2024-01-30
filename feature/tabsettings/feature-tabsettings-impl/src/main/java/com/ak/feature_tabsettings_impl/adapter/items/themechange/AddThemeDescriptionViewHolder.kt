package com.ak.feature_tabsettings_impl.adapter.items.themechange

import android.view.View
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.feature_tabsettings_impl.R

class AddThemeDescriptionViewHolder(
    itemView: View,
    private val onAddTheme: () -> Unit,
) : CustomThemeRecyclerViewHolder(itemView) {

    private val root by lazy { itemView.findViewById<View>(R.id.cvAddUserCustomThemeRoot) }
    private val addThemeBtn by lazy { itemView.findViewById<View>(R.id.ivAddUserCustomTheme) }

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyBackgroundTint(theme, root, R.attr.themedSecondaryBackgroundColor)
        CustomThemeApplier.applyTint(theme, addThemeBtn, R.attr.themedAccentColor)
    }

    fun bind() {
        itemView.setOnClickListener { onAddTheme() }
    }
}