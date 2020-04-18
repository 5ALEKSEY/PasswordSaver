package com.ak.feature_tabsettings_impl.main

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

interface ISettingsView : IBaseAppView {
    fun showDesignSettings()
    fun showPrivacySettings()
    fun showAboutScreen()
    fun startAuthAndOpenPrivacySettings()
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}