package com.ak.feature_tabsettings_impl.adapter

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.settings_item_text_layout.view.tvNewBadgeText
import kotlinx.android.synthetic.main.settings_item_text_layout.view.tvSettingName

abstract class BaseSettingsViewHolder<ItemModel : SettingsListItemModel>(itemView: View) :
    CustomThemeRecyclerViewHolder(itemView) {

    override fun applyTheme(theme: CustomTheme) {
        CustomThemeApplier.applyTextColor(theme, R.attr.themedPrimaryTextColor, itemView.tvSettingName)
        CustomThemeApplier.applyTextColor(theme, R.attr.staticColorWhite, itemView.tvNewBadgeText)
    }

    fun bindViewHolder(itemModel: ItemModel) {
        itemView.tvSettingName.text = itemModel.settingName
        itemView.tvNewBadgeText.isVisible = itemModel.hasNewBadge
        setViewHolderData(itemModel)
    }

    abstract fun setViewHolderData(itemModel: ItemModel)
}