package com.ak.feature_tabsettings_impl.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewHolder
import com.ak.base.adapter.AdapterDelegatesManager
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.sections.SectionAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.themechange.ThemeChangeAdapterDelegate
import com.ak.feature_tabsettings_impl.adapter.items.themechange.ThemeChangeSettingsListItemModel

class SettingsRecyclerViewAdapter constructor(
    private val onSwitchSettingsChanged: ((settingId: Int, isChecked: Boolean) -> Unit)? = null,
    private val onSpinnerSettingsChanged: ((settingId: Int, newDataId: Int) -> Unit)? = null,
    private val onSectionSettingsClicked: ((settingId: Int) -> Unit)? = null,
    private val onTextSettingsClicked: ((settingId: Int) -> Unit)? = null,
    private val onThemeChanged: ((newThemeId: Int) -> Unit)? = null,
    private val onAddTheme: ((settingId: Int) -> Unit)? = null,
    private val onEditTheme: ((themeId: Int) -> Unit)? = null,
    private val onDeleteTheme: ((themeId: Int) -> Unit)? = null,
) : CustomThemeRecyclerViewAdapter<CustomThemeRecyclerViewHolder>() {

    companion object {
        const val SWITCH_SETTING_TYPE = 1
        const val SPINNER_SETTING_TYPE = 2
        const val SECTION_SETTING_TYPE = 3
        const val TEXT_SETTING_TYPE = 4
        const val THEME_CHANGE_SETTING_TYPE = 5
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
                mAdapterSwitchSettingsChangedListener,
            ))
        }
        if (onSpinnerSettingsChanged != null) {
            adapterDelegatesManager.addDelegate(SpinnerAdapterDelegate(
                SPINNER_SETTING_TYPE,
                mAdapterSpinnerSettingsChangedListener,
            ))
        }
        if (onSectionSettingsClicked != null) {
            adapterDelegatesManager.addDelegate(SectionAdapterDelegate(
                SECTION_SETTING_TYPE,
                onSectionSettingsClicked,
            ))
        }
        if (onTextSettingsClicked != null) {
            adapterDelegatesManager.addDelegate(
                TextAdapterDelegate(
                    TEXT_SETTING_TYPE,
                    onTextSettingsClicked,
                )
            )
        }
        if (onThemeChanged != null
            && onAddTheme != null
            && onEditTheme != null
            && onDeleteTheme != null
        ) {
            adapterDelegatesManager.addDelegate(
                ThemeChangeAdapterDelegate(
                    THEME_CHANGE_SETTING_TYPE,
                    onThemeChanged,
                    onAddTheme,
                    onEditTheme,
                    onDeleteTheme,
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        adapterDelegatesManager.onCreateViewHolder(parent, viewType)

    override fun getItemViewType(position: Int) =
        adapterDelegatesManager.getItemViewType(settingsItemsList[position])

    override fun getItemCount() = settingsItemsList.size

    override fun onBindViewHolder(theme: CustomTheme, viewHolder: CustomThemeRecyclerViewHolder, position: Int) {
        adapterDelegatesManager.onBindViewHolder(settingsItemsList[position], viewHolder, theme)
    }

    fun addSettingsList(settingItems: List<SettingsListItemModel>) {
        val diffCallback = SettingsDiffUtilCallback(
            settingsItemsList,
            settingItems,
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        settingsItemsList.clear()
        settingsItemsList.addAll(settingItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class SettingsDiffUtilCallback(
        private val oldList: List<SettingsListItemModel>,
        private val newList: List<SettingsListItemModel>,
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
            val defaultCondition = isSameId && isSameName
            if (oldItem is SwitchSettingsListItemModel && newItem is SwitchSettingsListItemModel) {
                return defaultCondition && oldItem.isChecked == newItem.isChecked
            }
            if (oldItem is SpinnerSettingsListItemModel && newItem is SpinnerSettingsListItemModel) {
                return defaultCondition && oldItem.selectedItemPosition == newItem.selectedItemPosition
            }
            if (oldItem is ThemeChangeSettingsListItemModel && newItem is ThemeChangeSettingsListItemModel) {
                return defaultCondition
                    && oldItem.selectedThemeId == newItem.selectedThemeId
                    && oldItem.themes == newItem.themes
                    && oldItem.shouldShowThemeExampleView == newItem.shouldShowThemeExampleView
            }
            return defaultCondition
        }
    }
}