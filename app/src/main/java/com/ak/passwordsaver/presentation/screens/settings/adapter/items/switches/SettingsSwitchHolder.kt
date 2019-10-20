package com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.bindView

class SwitchAdapterDelegate(private val viewType: Int) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SwitchSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_switch_layout, parent, false)
        return SettingsSwitchHolder(itemView)
    }

    override fun onBindViewHolder(item: SettingsListItemModel, viewHolder: RecyclerView.ViewHolder) {
        val itemModel = item as SwitchSettingsListItemModel
        val holder = viewHolder as SettingsSwitchHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsSwitchHolder(itemView: View) : BaseSettingsViewHolder<SwitchSettingsListItemModel>(itemView) {

    private val mSwitch: Switch by bindView(R.id.s_setting_enabling_state)
    private val mDescription: TextView by bindView(R.id.tv_setting_description)

    override fun setViewHolderData(itemModel: SwitchSettingsListItemModel) {
        mSwitch.isChecked = itemModel.isChecked
        mDescription.text = itemModel.settingDescription
    }
}