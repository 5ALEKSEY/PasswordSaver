package com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.utils.bindView

class SettingsSpinnerHolder(
    itemView: View,
    val onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit
) : BaseSettingsViewHolder<SpinnerSettingsListItemModel>(itemView) {

    private val mSpinner: Spinner by bindView(R.id.s_setting_spinner_chooser)

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
    }
}