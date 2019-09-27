package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegatesManager
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchAdapterDelegate

class SettingsRecyclerViewAdapter(onSpinnerSettingsChanged: (settingId: Int, newDataId: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SWITCH_SETTING_TYPE = 1
        const val SPINNER_SETTING_TYPE = 2
    }

    private val mSettingsItemsList = arrayListOf<SettingsListItemModel>()
    private val mAdapterDelegatesManager = AdapterDelegatesManager<SettingsListItemModel>()

    init {
        mAdapterDelegatesManager.addDelegate(SwitchAdapterDelegate(SWITCH_SETTING_TYPE))
        mAdapterDelegatesManager.addDelegate(SpinnerAdapterDelegate(SPINNER_SETTING_TYPE, onSpinnerSettingsChanged))
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
            mSettingsItemsList.addAll(settingItems)
            notifyDataSetChanged()
        }
    }
}