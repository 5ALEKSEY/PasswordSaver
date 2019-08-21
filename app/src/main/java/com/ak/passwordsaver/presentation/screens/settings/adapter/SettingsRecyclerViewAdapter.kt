package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SettingsSwitchHolder
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel

class SettingsRecyclerViewAdapter : RecyclerView.Adapter<BaseSettingsViewHolder<*>>() {

    private val mSettingsItemsList = arrayListOf<SettingsListItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSettingsViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SettingsListItemModel.SWITCH_SETTING_TYPE -> {
                val view = inflater.inflate(R.layout.settings_item_switch_layout, parent, false)
                SettingsSwitchHolder(view)
            }
            else -> {
                // default setting model
                val view = inflater.inflate(R.layout.settings_item_switch_layout, parent, false)
                SettingsSwitchHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int) = mSettingsItemsList[position].settingType

    override fun getItemCount() = mSettingsItemsList.size

    override fun onBindViewHolder(viewHolder: BaseSettingsViewHolder<*>, position: Int) {
        when (getItemViewType(position)) {
            SettingsListItemModel.SWITCH_SETTING_TYPE -> {
                (viewHolder as SettingsSwitchHolder).bindViewHolder(mSettingsItemsList[position] as SwitchSettingsListItemModel)
            }
        }
    }

    fun addSettingsList(settingItems: List<SettingsListItemModel>) {
        if (!settingItems.isNullOrEmpty()) {
            mSettingsItemsList.addAll(settingItems)
            notifyDataSetChanged()
        }
    }
}