package com.ak.passwordsaver.presentation.screens.settings.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ak.passwordsaver.presentation.base.adapter.AdapterDelegatesManager
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.sections.SectionAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchAdapterDelegate
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel
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

    private val mAdapterSwitchSettingsChangedListener: (settingId: Int, isChecked: Boolean) -> Unit =
        { position, newState ->
            val itemModel = mSettingsItemsList[position]
            if (itemModel is SwitchSettingsListItemModel) {
                itemModel.isChecked = newState
            }
            mSettingsItemsList[position] = itemModel
            onSwitchSettingsChanged?.invoke(itemModel.settingId, newState)
        }
    private val mAdapterSpinnerSettingsChangedListener: (settingId: Int, newDataId: Int) -> Unit =
        { position, newSelectedPosition ->
            val itemModel = mSettingsItemsList[position]
            if (itemModel is SpinnerSettingsListItemModel) {
                itemModel.selectedItemPosition = newSelectedPosition
            }
            mSettingsItemsList[position] = itemModel
            onSpinnerSettingsChanged?.invoke(itemModel.settingId, newSelectedPosition)
        }

    init {
        if (onSwitchSettingsChanged != null) {
            mAdapterDelegatesManager.addDelegate(
                SwitchAdapterDelegate(
                    SWITCH_SETTING_TYPE,
                    mAdapterSwitchSettingsChangedListener
                )
            )
        }
        if (onSpinnerSettingsChanged != null) {
            mAdapterDelegatesManager.addDelegate(
                SpinnerAdapterDelegate(
                    SPINNER_SETTING_TYPE,
                    mAdapterSpinnerSettingsChangedListener
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
        val diffCallback = PrivacyDiffUtilCallback(mSettingsItemsList, settingItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mSettingsItemsList.clear()
        mSettingsItemsList.addAll(settingItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class PrivacyDiffUtilCallback(
        private val oldList: List<SettingsListItemModel>,
        private val newList: List<SettingsListItemModel>
    ) : DiffUtil.Callback() {

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