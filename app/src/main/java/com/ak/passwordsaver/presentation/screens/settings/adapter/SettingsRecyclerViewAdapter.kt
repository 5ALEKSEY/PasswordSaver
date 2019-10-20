package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegatesManager
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.sections.SectionAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.texts.TextAdapterDelegate

class SettingsRecyclerViewAdapter constructor(
    onSwitchSettingsChanged: ((settingId: Int, isChecked: Boolean) -> Unit)? = null,
    onSpinnerSettingsChanged: ((settingId: Int, newDataId: Int) -> Unit)? = null,
    onSectionSettingsClicked: ((settingId: Int) -> Unit)? = null,
    onTextSettingsClicked: ((settingId: Int) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SWITCH_SETTING_TYPE = 1
        const val SPINNER_SETTING_TYPE = 2
        const val SECTION_SETTING_TYPE = 3
        const val TEXT_SETTING_TYPE = 4
    }

    private val mSettingsItemsList = arrayListOf<SettingsListItemModel>()
    private val mAdapterDelegatesManager = AdapterDelegatesManager<SettingsListItemModel>()

    init {
        if (onSwitchSettingsChanged != null) {
            mAdapterDelegatesManager.addDelegate(
                SwitchAdapterDelegate(
                    SWITCH_SETTING_TYPE,
                    onSwitchSettingsChanged
                )
            )
        }
        if (onSpinnerSettingsChanged != null) {
            mAdapterDelegatesManager.addDelegate(
                SpinnerAdapterDelegate(
                    SPINNER_SETTING_TYPE,
                    onSpinnerSettingsChanged
                )
            )
        }
        if (onSectionSettingsClicked != null) {
            mAdapterDelegatesManager.addDelegate(
                SectionAdapterDelegate(
                    SECTION_SETTING_TYPE,
                    onSectionSettingsClicked
                )
            )
        }
        if (onTextSettingsClicked != null) {
            mAdapterDelegatesManager.addDelegate(
                TextAdapterDelegate(
                    TEXT_SETTING_TYPE,
                    onTextSettingsClicked
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        mAdapterDelegatesManager.onCreateViewHolder(parent, viewType)


    override fun getItemViewType(position: Int) =
        mAdapterDelegatesManager.getItemViewType(mSettingsItemsList[position])

    override fun getItemCount() = mSettingsItemsList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        mAdapterDelegatesManager.onBindViewHolder(mSettingsItemsList[position], viewHolder)
    }

    fun addSettingsList(settingItems: List<SettingsListItemModel>) {
        if (!settingItems.isNullOrEmpty()) {
            mSettingsItemsList.clear()
            mSettingsItemsList.addAll(settingItems)
            notifyDataSetChanged()
        }
    }
}