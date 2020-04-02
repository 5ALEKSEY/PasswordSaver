package com.ak.settings.adapter.items.switches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.adapter.AdapterDelegate
import com.ak.settings.R
import com.ak.settings.adapter.BaseSettingsViewHolder
import com.ak.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.settings_item_switch_layout.view.*

class SwitchAdapterDelegate(
    private val viewType: Int,
    private val onSwitchSettingsChanged: (settingId: Int, isChecked: Boolean) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SwitchSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_switch_layout, parent, false)
        return SettingsSwitchHolder(
            itemView,
            onSwitchSettingsChanged
        )
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: RecyclerView.ViewHolder
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

    override fun setViewHolderData(itemModel: SwitchSettingsListItemModel) {
        itemView.sSettingEnablingState.isChecked = itemModel.isChecked
        itemView.tvSettingDescription.text = itemModel.settingDescription
        itemView.setOnClickListener {
            itemView.sSettingEnablingState.isChecked = !itemView.sSettingEnablingState.isChecked
        }
        itemView.sSettingEnablingState.setOnCheckedChangeListener { _, isChecked ->
            onSwitchSettingsChanged.invoke(adapterPosition, isChecked)
        }
    }
}