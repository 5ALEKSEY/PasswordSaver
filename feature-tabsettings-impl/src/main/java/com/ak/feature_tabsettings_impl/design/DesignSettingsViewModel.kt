package com.ak.feature_tabsettings_impl.design

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.toDescription
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.themechange.ThemeChangeSettingsListItemModel
import javax.inject.Inject

class DesignSettingsViewModel @Inject constructor(
    private val settingsPrefManager: ISettingsPreferencesManager,
    private val resourceManager: IResourceManager,
) : BasePSViewModel() {

    private val designSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()

    fun subscribeToDesignSettingsListLiveData(): LiveData<List<SettingsListItemModel>> = designSettingsListLiveData

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID -> {
                settingsPrefManager.setChangeThemeWithAnimationEnabledState(isChecked)
            }
            else -> {
                // no op
            }
        }
    }

    fun loadSettingsData() {
        val themeChangeSettingsModel = ThemeChangeSettingsListItemModel(
            THEME_CHANGE_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_setting_name),
            CustomThemeManager.getInstance().getAvailableThemes(),
            CustomThemeManager.getCurrentTheme().toDescription(),
        )
        val themeChangeWithAnimationModel = SwitchSettingsListItemModel(
            THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_with_animation_setting_name),
            resourceManager.getString(R.string.change_theme_with_animation_setting_desc),
            settingsPrefManager.isChangeThemeWithAnimationEnabled(),
        )

        designSettingsListLiveData.value = listOf(
            themeChangeSettingsModel,
            themeChangeWithAnimationModel,
        )
    }

    fun onThemeChanged(theme: CustomTheme.Description) {
        CustomThemeManager.getInstance().setTheme(theme.id)
    }

    companion object {
        private const val THEME_CHANGE_SETTINGS_ID = 1
        private const val THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID = 2
    }
}