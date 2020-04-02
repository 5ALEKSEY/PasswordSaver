package com.ak.settings.design

import com.ak.base.ui.IBaseAppView
import com.ak.settings.adapter.items.SettingsListItemModel

interface IDesignSettingsView : IBaseAppView {
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}