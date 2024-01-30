package com.ak.feature_tabsettings_impl.adapter.items.spinners

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegate
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.BaseSettingsViewHolder
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

class SpinnerAdapterDelegate(
    private val viewType: Int,
    private val onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit
) : AdapterDelegate<SettingsListItemModel> {

    override fun isForViewType(item: SettingsListItemModel) = item is SpinnerSettingsListItemModel

    override fun getItemViewType() = viewType

    override fun onCreateViewHolder(parent: ViewGroup): CustomThemeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.settings_item_spinner_layout, parent, false)
        return SettingsSpinnerHolder(
            itemView,
            onSpinnerSettingsChanged
        )
    }

    override fun onBindViewHolder(
        item: SettingsListItemModel,
        viewHolder: CustomThemeRecyclerViewHolder,
        theme: CustomTheme,
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

    private val tvSettingDescription by lazy { itemView.findViewById<TextView>(R.id.tvSettingDescription) }
    private val sSettingSpinnerChooser by lazy { itemView.findViewById<Spinner>(R.id.sSettingSpinnerChooser) }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedSecondaryTextColor,
            tvSettingDescription,
        )
    }

    override fun setViewHolderData(itemModel: SpinnerSettingsListItemModel) {
        val arrayAdapter = ArrayAdapter<String>(itemView.context, R.layout.default_spinner_item)
        val spinnerItems = itemModel.spinnerItems
        arrayAdapter.addAll(spinnerItems)
        itemView.setOnClickListener { sSettingSpinnerChooser.performClick() }
        sSettingSpinnerChooser.adapter = arrayAdapter
        sSettingSpinnerChooser.setSelection(itemModel.selectedItemPosition)
        sSettingSpinnerChooser.onItemSelectedListener =
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
        tvSettingDescription.text = itemModel.settingDescription
    }
}