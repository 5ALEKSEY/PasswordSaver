package com.ak.passwordsaver.presentation.screens.settings

import com.ak.passwordsaver.presentation.base.IBaseAppView
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

interface ISettingsView : IBaseAppView {
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}