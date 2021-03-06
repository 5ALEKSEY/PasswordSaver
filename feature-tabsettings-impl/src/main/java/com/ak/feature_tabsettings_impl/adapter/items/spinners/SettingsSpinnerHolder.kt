package com.ak.feature_tabsettings_impl.adapter.items.spinners

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.BaseSettingsViewHolder
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.settings_item_spinner_layout.view.*

class SpinnerAdapterDelegate(
    private val viewType: Int,
    private val onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SpinnerSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_spinner_layout, parent, false)
        return SettingsSpinnerHolder(
            itemView,
            onSpinnerSettingsChanged
        )
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: RecyclerView.ViewHolder
    ) {
        val itemModel = item as SpinnerSettingsListItemModel
        val holder = viewHolder as SettingsSpinnerHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsSpinnerHolder(
    itemView: View,
    private val onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit
) : BaseSettingsViewHolder<SpinnerSettingsListItemModel>(itemView) {

    override fun setViewHolderData(itemModel: SpinnerSettingsListItemModel) {
        val arrayAdapter = ArrayAdapter<String>(itemView.context, R.layout.default_spinner_item)
        val spinnerItems = itemModel.spinnerItems
        arrayAdapter.addAll(spinnerItems)
        itemView.setOnClickListener { itemView.sSettingSpinnerChooser.performClick() }
        itemView.sSettingSpinnerChooser.adapter = arrayAdapter
        itemView.sSettingSpinnerChooser.setSelection(itemModel.selectedItemPosition)
        itemView.sSettingSpinnerChooser.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onSpinnerSettingsChanged.invoke(adapterPosition, position)
                }
            }
        itemView.tvSettingDescription.text = itemModel.settingDescription
    }
}