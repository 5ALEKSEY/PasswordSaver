package com.ak.passwordsaver.presentation.screens.settings.privacy

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

interface IPrivacySettingsView : IBaseAppView {
    fun openAddPincodeScreen()
    fun openChangePincodeScreen()
    fun openAddPatternScreen()
    fun openChangePatternScreen()
    fun displayAppSettings(settingsItems: List<SettingsListItemModel>)
}