package com.ak.feature_tabsettings_impl.adapter

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    CustomThemeRecyclerViewHolder(itemView) {

    private val tvNewBadgeText by lazy { itemView.findViewById<TextView>(R.id.tvNewBadgeText) }
    private val tvSettingName by lazy { itemView.findViewById<TextView>(R.id.tvSettingName) }

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyTextColor(theme, R.attr.themedPrimaryTextColor, tvSettingName)
        CustomThemeApplier.applyTextColor(theme, R.attr.staticColorWhite, tvNewBadgeText)
        CustomThemeApplier.applyBackgroundTint(theme, itemView, R.attr.themedAccentColor)
    }

    open fun bindViewHolder(itemModel: ItemModel) {
        tvSettingName.text = itemModel.settingName
        tvNewBadgeText.isVisible = itemModel.hasNewBadge
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}