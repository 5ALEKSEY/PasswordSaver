package com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.screens.settings.adapter.BaseSettingsViewHolder
import com.ak.passwordsaver.utils.bindView

class SettingsSpinnerHolder(itemView: View) : BaseSettingsViewHolder<SpinnerSettingsListItemModel>(itemView) {

    private val mSpinner: Spinner by bindView(R.id.s_setting_spinner_chooser)

    override fun setViewHolderData(itemModel: SpinnerSettingsListItemModel) {
        val arrayAdapter = ArrayAdapter<String>(itemView.context, R.layout.default_spinner_item)
        arrayAdapter.addAll(itemModel.spinnerItems)
        mSpinner.adapter = arrayAdapter
        mSpinner.setSelection(itemModel.selectedItemPosition)
    }
}