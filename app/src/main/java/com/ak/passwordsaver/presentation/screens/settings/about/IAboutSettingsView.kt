package com.ak.passwordsaver.presentation.screens.settings.about

import com.ak.passwordsaver.presentation.base.ui.IBaseAppView
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel

interface IAboutSettingsView : IBaseAppView {
    fun setVersionInfo(versionInfo: String)
    fun displayAboutActions(settingsItems: List<SettingsListItemModel>)
    fun startReportAction()
}