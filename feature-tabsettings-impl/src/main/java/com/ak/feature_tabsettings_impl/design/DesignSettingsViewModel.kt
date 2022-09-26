package com.ak.feature_tabsettings_impl.design

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.CustomThemePreferencesMng
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.themechange.ThemeChangeSettingsListItemModel
import javax.inject.Inject

class DesignSettingsViewModel @Inject constructor(
    private val settingsPrefManager: ISettingsPreferencesManager,
    private val resourceManager: IResourceManager,
) : BasePSViewModel() {

    private val selectedThemeId get() = CustomThemeManager.getCurrentSelectedTheme().id

    private val designSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()
    private val openNativeThemeConfigDialogLiveData = SingleEventLiveData<Boolean>()
    private val changeThemeLiveData = SingleEventLiveData<Int>()

    fun subscribeToDesignSettingsListLiveData(): LiveData<List<SettingsListItemModel>> = designSettingsListLiveData
    fun subscribeToOpenNativeThemeConfigDialogLiveData(): LiveData<Boolean> = openNativeThemeConfigDialogLiveData
    fun subscribeToChangeThemeLiveData(): LiveData<Int> = changeThemeLiveData

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID -> {
                settingsPrefManager.setChangeThemeWithAnimationEnabledState(isChecked)
            }
            THEME_USE_NATIVE_SETTINGS_ID -> {
                Log.d("TEST", "onSwitchSettingsItemChanged. isChecked=$isChecked")
                val newThemeId = if (isChecked) {
                    CustomThemeManager.NATIVE_THEME_ID
                } else {
                    CustomThemeManager.DEFAULT_THEME_ID
                }
                onThemeChanged(newThemeId)
                if (!isChecked) loadSettingsData()
            }
            else -> {
                // no op
            }
        }
    }

    fun onSettingTextItemClicked(settingId: Int) {
        when (settingId) {
            THEME_CHANGE_NATIVE_CONFIG_SETTINGS_ID -> {
                openNativeThemeConfigDialogLiveData.value = false
            }
            else -> {
                // no op
            }
        }
    }

    fun loadSettingsData() {
        Log.d("TEST", "loadSettingsData")
        val availableThemes = CustomThemeManager.getInstance()
            .getAvailableThemes()
            .filter { it.id != CustomThemeManager.NATIVE_THEME_ID }
        val themeChangeSettingsModel = ThemeChangeSettingsListItemModel(
            THEME_CHANGE_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_setting_name),
            availableThemes,
            CustomThemeManager.getCurrentSelectedTheme().id,
        )

        val themeUseNativeTheme = SwitchSettingsListItemModel(
            THEME_USE_NATIVE_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_use_native_setting_name),
            resourceManager.getString(R.string.change_theme_use_native_setting_desc),
            selectedThemeId.isNativeThemeId(),
        )

        val themeChangeNativeConfiguration = if (selectedThemeId.isNativeThemeId()) {
            TextSettingsListItemModel(
                THEME_CHANGE_NATIVE_CONFIG_SETTINGS_ID,
                resourceManager.getString(R.string.change_theme_change_native_config_setting_name),
            )
        } else {
            null
        }

        val themeChangeWithAnimationModel = SwitchSettingsListItemModel(
            THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_with_animation_setting_name),
            resourceManager.getString(R.string.change_theme_with_animation_setting_desc),
            settingsPrefManager.isChangeThemeWithAnimationEnabled(),
        )

        designSettingsListLiveData.value = listOfNotNull(
            themeChangeSettingsModel,
            themeUseNativeTheme,
            themeChangeNativeConfiguration,
            themeChangeWithAnimationModel,
        )
    }

    fun onThemeChanged(themeId: Int) {
        if (themeId.isNativeThemeId()) {
            openNativeThemeConfigDialogLiveData.value = true
        } else {
            changeThemeLiveData.value = themeId
        }
    }

    fun onNativeThemeConfigured(lightThemeId: Int, darkThemeId: Int) {
        val shouldRefreshSettingsList = selectedThemeId != CustomThemeManager.NATIVE_THEME_ID

        CustomThemePreferencesMng.setNativeLightThemeId(lightThemeId)
        CustomThemePreferencesMng.setNativeDarkThemeId(darkThemeId)
        changeThemeLiveData.value = CustomThemeManager.NATIVE_THEME_ID

        if (shouldRefreshSettingsList) loadSettingsData()
    }

    fun onNativeThemeConfigurationSkipped() {
        loadSettingsData()
    }

    private fun Int.isNativeThemeId() = this == CustomThemeManager.NATIVE_THEME_ID

    companion object {
        private const val THEME_CHANGE_SETTINGS_ID = 0
        private const val THEME_USE_NATIVE_SETTINGS_ID = 1
        private const val THEME_CHANGE_NATIVE_CONFIG_SETTINGS_ID = 2
        private const val THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID = 3
    }
}