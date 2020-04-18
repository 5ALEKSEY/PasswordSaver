package com.ak.feature_tabsettings_impl.design

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

interface IDesignSettingsView : IBaseAppView {
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}