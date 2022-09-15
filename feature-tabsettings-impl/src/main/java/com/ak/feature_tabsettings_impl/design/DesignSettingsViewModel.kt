package com.ak.feature_tabsettings_impl.design

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.toDescription
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.themechange.ThemeChangeSettingsListItemModel
import javax.inject.Inject

class DesignSettingsViewModel @Inject constructor(
    private val resourceManager: IResourceManager,
) : BasePSViewModel() {

    private val designSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()

    fun subscribeToDesignSettingsListLiveData(): LiveData<List<SettingsListItemModel>> = designSettingsListLiveData

    fun loadSettingsData() {
        val themeChangeSettingsModel = ThemeChangeSettingsListItemModel(
            THEME_CHANGE_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_setting_name),
            CustomThemeManager.getInstance().getAvailableThemes(),
            CustomThemeManager.getCurrentTheme().toDescription(),
            true,
        )
        designSettingsListLiveData.value = listOf(themeChangeSettingsModel)
    }

    fun onThemeChanged(theme: CustomTheme.Description) {
        CustomThemeManager.getInstance().setTheme(theme.id)
    }

    companion object {
        private const val THEME_CHANGE_SETTINGS_ID = 1
    }
}