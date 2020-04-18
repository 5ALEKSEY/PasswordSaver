package com.ak.feature_tabsettings_impl.about

import com.ak.base.ui.IBaseAppView
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel

interface IAboutSettingsView : IBaseAppView {
    fun setVersionInfo(versionInfo: String)
    fun displayAboutActions(settingsItems: List<SettingsListItemModel>)
    fun startReportAction()
}