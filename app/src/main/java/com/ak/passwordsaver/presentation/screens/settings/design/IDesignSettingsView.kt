package com.ak.passwordsaver.presentation.screens.settings.design

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

interface IDesignSettingsView : IBaseAppView {
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}