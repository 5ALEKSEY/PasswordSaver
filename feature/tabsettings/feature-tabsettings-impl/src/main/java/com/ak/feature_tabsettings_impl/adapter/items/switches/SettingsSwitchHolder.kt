package com.ak.feature_tabsettings_impl.adapter.items.switches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.BaseSettingsViewHolder
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class SwitchAdapterDelegate(
    private val viewType: Int,
    private val onSwitchSettingsChanged: (settingId: Int, isChecked: Boolean) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SwitchSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_switch_layout, parent, false)
        return SettingsSwitchHolder(
            itemView,
            onSwitchSettingsChanged
        )
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
    ) {
        val itemModel = item as SwitchSettingsListItemModel
        val holder = viewHolder as SettingsSwitchHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsSwitchHolder(
    itemView: View,
    private val onSwitchSettingsChanged: (settingId: Int, isChecked: Boolean) -> Unit
) : BaseSettingsViewHolder<SwitchSettingsListItemModel>(itemView) {

    private val tvSettingDescription by lazy { itemView.findViewById<TextView>(R.id.tvSettingDescription) }
    private val sSettingEnablingState by lazy { itemView.findViewById<SwitchCompat>(R.id.sSettingEnablingState) }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedSecondaryTextColor,
            tvSettingDescription,
        )
        CustomThemeApplier.applyForSwitch(
            theme,
            sSettingEnablingState,
            R.attr.themedSwitchThumbUncheckedColor,
            R.attr.themedSwitchThumbCheckedColor,
            R.attr.themedSwitchTrackUncheckedColor,
            R.attr.themedSwitchTrackCheckedColor,
        )
    }

    override fun setViewHolderData(itemModel: SwitchSettingsListItemModel) {
        sSettingEnablingState.isChecked = itemModel.isChecked
        tvSettingDescription.text = itemModel.settingDescription
        itemView.setOnClickListener {
            val newState = !sSettingEnablingState.isChecked
            sSettingEnablingState.isChecked = newState
            onSwitchSettingsChanged.invoke(adapterPosition, newState)
        }
        sSettingEnablingState.setOnClickListener {
            onSwitchSettingsChanged.invoke(adapterPosition, sSettingEnablingState.isChecked)
        }
    }
}