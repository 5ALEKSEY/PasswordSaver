package com.ak.feature_tabsettings_impl.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.adapter.AdapterDelegatesManager
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.sections.SectionAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextAdapterDelegate

class SettingsRecyclerViewAdapter constructor(
    private val onSwitchSettingsChanged: ((settingId: Int, isChecked: Boolean) -> Unit)? = null,
    private val onSpinnerSettingsChanged: ((settingId: Int, newDataId: Int) -> Unit)? = null,
    private val onSectionSettingsClicked: ((settingId: Int) -> Unit)? = null,
    private val onTextSettingsClicked: ((settingId: Int) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SWITCH_SETTING_TYPE = 1
        const val SPINNER_SETTING_TYPE = 2
        const val SECTION_SETTING_TYPE = 3
        const val TEXT_SETTING_TYPE = 4
    }

    private val settingsItemsList = arrayListOf<SettingsListItemModel>()
    private val adapterDelegatesManager = AdapterDelegatesManager<SettingsListItemModel>()

    private val mAdapterSwitchSettingsChangedListener: (settingId: Int, isChecked: Boolean) -> Unit =
        { position, newState ->
            val itemModel = settingsItemsList[position]
            if (itemModel is SwitchSettingsListItemModel) {
                itemModel.isChecked = newState
            }
            settingsItemsList[position] = itemModel
            onSwitchSettingsChanged?.invoke(itemModel.settingId, newState)
        }
    private val mAdapterSpinnerSettingsChangedListener: (settingId: Int, newDataId: Int) -> Unit =
        { position, newSelectedPosition ->
            val itemModel = settingsItemsList[position]
            if (itemModel is SpinnerSettingsListItemModel) {
                itemModel.selectedItemPosition = newSelectedPosition
            }
            settingsItemsList[position] = itemModel
            onSpinnerSettingsChanged?.invoke(itemModel.settingId, newSelectedPosition)
        }

    init {
        if (onSwitchSettingsChanged != null) {
            adapterDelegatesManager.addDelegate(SwitchAdapterDelegate(
                    SWITCH_SETTING_TYPE,
                    mAdapterSwitchSettingsChangedListener
            ))
        }
        if (onSpinnerSettingsChanged != null) {
            adapterDelegatesManager.addDelegate(SpinnerAdapterDelegate(
                    SPINNER_SETTING_TYPE,
                    mAdapterSpinnerSettingsChangedListener
            ))
        }
        if (onSectionSettingsClicked != null) {
            adapterDelegatesManager.addDelegate(SectionAdapterDelegate(
                    SECTION_SETTING_TYPE,
                    onSectionSettingsClicked
            ))
        }
        if (onTextSettingsClicked != null) {
            adapterDelegatesManager.addDelegate(TextAdapterDelegate(
                    TEXT_SETTING_TYPE,
                    onTextSettingsClicked
            ))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        adapterDelegatesManager.onCreateViewHolder(parent, viewType)


    override fun getItemViewType(position: Int) =
        adapterDelegatesManager.getItemViewType(settingsItemsList[position])

    override fun getItemCount() = settingsItemsList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        adapterDelegatesManager.onBindViewHolder(settingsItemsList[position], viewHolder)
    }

    fun addSettingsList(settingItems: List<SettingsListItemModel>) {
        val diffCallback = PrivacyDiffUtilCallback(
                settingsItemsList,
                settingItems
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        settingsItemsList.clear()
        settingsItemsList.addAll(settingItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class PrivacyDiffUtilCallback(
        private val oldList: List<SettingsListItemModel>,
        private val newList: List<SettingsListItemModel>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].settingId == newList[newItemPosition].settingId

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val isSameId = oldItem.settingId == newItem.settingId
            val isSameName = oldItem.settingName.contentEquals(newItem.settingName)
            if (oldItem is SwitchSettingsListItemModel && newItem is SwitchSettingsListItemModel) {
                return isSameId && isSameName && (oldItem.isChecked == newItem.isChecked)
            }
            if (oldItem is SpinnerSettingsListItemModel && newItem is SpinnerSettingsListItemModel) {
                return isSameId && isSameName && (oldItem.selectedItemPosition == newItem.selectedItemPosition)
            }
            return isSameId && isSameName
        }
    }
}