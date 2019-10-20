package com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.bindView

class SpinnerAdapterDelegate(
    private val viewType: Int,
    private val onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SpinnerSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_spinner_layout, parent, false)
        return SettingsSpinnerHolder(itemView, onSpinnerSettingsChanged)
    }

    override fun onBindViewHolder(item: SettingsListItemModel, viewHolder: RecyclerView.ViewHolder) {
        val itemModel = item as SpinnerSettingsListItemModel
        val holder = viewHolder as SettingsSpinnerHolder
        holder.bindViewHolder(itemModel)
    }
}

class SettingsSpinnerHolder(
    itemView: View,
    private val onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit
) : BaseSettingsViewHolder<SpinnerSettingsListItemModel>(itemView) {

    private val mSpinner: Spinner by bindView(R.id.s_setting_spinner_chooser)
    private val mDescription: TextView by bindView(R.id.tv_setting_description)

    override fun setViewHolderData(itemModel: SpinnerSettingsListItemModel) {
        val arrayAdapter = ArrayAdapter<String>(itemView.context, R.layout.default_spinner_item)
        val spinnerItems = itemModel.spinnerItems
        arrayAdapter.addAll(spinnerItems)
        mSpinner.adapter = arrayAdapter
        mSpinner.setSelection(itemModel.selectedItemPosition)
        mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onSpinnerSettingsChanged.invoke(itemModel.settingId, position)
            }
        }
        mDescription.text = itemModel.settingDescription
    }
}