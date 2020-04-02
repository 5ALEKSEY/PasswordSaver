package com.ak.settings.main

import com.ak.base.ui.IBaseAppView
import com.ak.settings.adapter.items.SettingsListItemModel

interface ISettingsView : IBaseAppView {
    fun showDesignSettings()
    fun showPrivacySettings()
    fun showAboutScreen()
    fun startAuthAndOpenPrivacySettings()
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}