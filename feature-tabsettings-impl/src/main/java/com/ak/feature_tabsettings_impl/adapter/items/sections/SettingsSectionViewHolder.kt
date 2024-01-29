package com.ak.feature_tabsettings_impl.adapter.items.sections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.BaseSettingsViewHolder
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class SectionAdapterDelegate(
    private val viewType: Int,
    private val onSectionSettingsClicked: (settingId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SectionSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_section_layout, parent, false)
        return SettingsSectionHolder(
            itemView,
            onSectionSettingsClicked
        )
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
    ) {
        val itemModel = item as SectionSettingsListItemModel
        val holder = viewHolder as SettingsSectionHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsSectionHolder(
    itemView: View,
    private val onSectionSettingsClicked: (settingId: Int) -> Unit
) : BaseSettingsViewHolder<SectionSettingsListItemModel>(itemView) {

    private val ivSettingsSectionImage by lazy { itemView.findViewById<ImageView>(R.id.ivSettingsSectionImage) }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyTint(
            theme,
            R.attr.themedPrimaryTextColor,
            ivSettingsSectionImage,
        )
    }

    override fun setViewHolderData(itemModel: SectionSettingsListItemModel) {
        ivSettingsSectionImage.setImageResource(itemModel.imageRes)
        itemView.setSafeClickListener { onSectionSettingsClicked(itemModel.settingId) }
    }
}