package com.ak.passwordsaver.presentation.screens.settings

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

interface ISettingsView : IBaseAppView {
    fun showDesignSettings()
    fun showPrivacySettings()
    fun showAboutScreen()
    fun startAuthAndOpenPrivacySettings()
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}