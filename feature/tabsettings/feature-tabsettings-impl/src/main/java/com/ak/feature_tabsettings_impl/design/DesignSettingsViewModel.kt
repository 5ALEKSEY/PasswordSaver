package com.ak.feature_tabsettings_impl.design

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeColorAttr
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.CustomThemePreferencesMng
import com.ak.app_theme.theme.ICustomThemesInitializer
import com.ak.app_theme.theme.toDescription
import com.ak.base.extensions.hexToIntColor
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.themechange.ThemeChangeSettingsListItemModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StaticFieldLeak")
class DesignSettingsViewModel @Inject constructor(
    private val appContext: Context,
    private val settingsPrefManager: ISettingsPreferencesManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
    private val userThemesRepo: ICustomUserThemesRepository,
    private val themesInitializer: ICustomThemesInitializer,
) : BasePSViewModel() {

    private val selectedThemeId get() = CustomThemeManager.getCurrentSelectedTheme().id

    private val designSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()
    private val openNativeThemeConfigDialogLiveData = SingleEventLiveData<Boolean>()
    private val changeThemeLiveData = SingleEventLiveData<Int>()
    private val openManageCustomThemeLiveData = SingleEventLiveData<Long?>()

    fun subscribeToDesignSettingsListLiveData(): LiveData<List<SettingsListItemModel>> = designSettingsListLiveData
    fun subscribeToOpenNativeThemeConfigDialogLiveData(): LiveData<Boolean> = openNativeThemeConfigDialogLiveData
    fun subscribeToChangeThemeLiveData(): LiveData<Int> = changeThemeLiveData
    fun subscribeToOpenManageCustomTheme(): LiveData<Long?> = openManageCustomThemeLiveData

    fun onSwitchSettingsItemChanged(settingId: Int, isChecked: Boolean) {
        when (settingId) {
            THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID -> {
                settingsPrefManager.setChangeThemeWithAnimationEnabledState(isChecked)
            }
            THEME_USE_NATIVE_SETTINGS_ID -> {
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
        viewModelScope.launch {
            val settingsModels = mutableListOf<SettingsListItemModel>()
            settingsModels.add(loadThemeSettingsModel())
            settingsModels.add(loadCustomUserThemeSettingModel())
            settingsModels.addAll(loadNativeThemeSettingModels())
            settingsModels.add(loadSwitchThemeWithAnimationSettingModel())
            designSettingsListLiveData.value = settingsModels
        }
    }

    private suspend fun loadThemeSettingsModel() = withContext(Dispatchers.IO) {
        featuresUpdateManager.markAppThemeFeatureAsViewed()
        val availableThemes = CustomThemeManager.getInstance()
            .getAvailableThemes()
            .filter { it.id != CustomThemeManager.NATIVE_THEME_ID && !it.isCustom }
            .map { it.toDescription() }
        return@withContext ThemeChangeSettingsListItemModel(
            settingId = THEME_APP_SETTINGS_ID,
            settingName = resourceManager.getString(R.string.app_themes_setting_name),
            themes = availableThemes,
            selectedThemeId = CustomThemeManager.getCurrentSelectedTheme().id,
            shouldShowThemeExampleView = true,
            hasNewBadge = !featuresUpdateManager.isAppThemeFeatureViewed(),
        )
    }

    private suspend fun loadCustomUserThemeSettingModel() = withContext(Dispatchers.IO) {
        val customUserThemes = getAllCustomUserThemes()
            .map { it.toDescription() }
        val themes = listOf(CREATE_USER_CUSTOM_THEME_ITEM) + customUserThemes
        return@withContext ThemeChangeSettingsListItemModel(
            settingId = THEME_USER_CUSTOM_SETTINGS_ID,
            settingName = resourceManager.getString(R.string.user_custom_themes_setting_name),
            themes = themes,
            selectedThemeId = CustomThemeManager.getCurrentSelectedTheme().id,
        )
    }

    private suspend fun getAllCustomUserThemes() = withContext(Dispatchers.IO) {
        userThemesRepo.getAllThemes().map { customUserTheme ->
            val themeStyle = if (customUserTheme.isLight()) {
                R.style.CustomTheme_Blue
            } else {
                R.style.CustomTheme_Orange
            }
            val builder = CustomTheme.Builder(appContext)
                .id(customUserTheme.getThemeId().toInt())
                .name(customUserTheme.getName())
                .themeStyle(themeStyle)
                .lightThemeFlag(customUserTheme.isLight())

            customUserTheme.getColorAttrs().forEach { customColorAttr ->
                builder.overrideColorAttr(
                    CustomThemeColorAttr.customIdToAndroid(customColorAttr.attrCustomId),
                    customColorAttr.colorHexValue.hexToIntColor(),
                )
            }

            return@map builder.build()
        }
    }

    private suspend fun loadNativeThemeSettingModels() = withContext(Dispatchers.IO) {
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

        return@withContext listOfNotNull(themeUseNativeTheme, themeChangeNativeConfiguration)
    }

    private suspend fun loadSwitchThemeWithAnimationSettingModel() = withContext(Dispatchers.IO) {
        return@withContext SwitchSettingsListItemModel(
            THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID,
            resourceManager.getString(R.string.change_theme_with_animation_setting_name),
            resourceManager.getString(R.string.change_theme_with_animation_setting_desc),
            settingsPrefManager.isChangeThemeWithAnimationEnabled(),
        )
    }

    fun onThemeChanged(themeId: Int) {
        if (themeId.isNativeThemeId()) {
            openNativeThemeConfigDialogLiveData.value = true
        } else {
            onThemeChangedInternal(themeId)
        }
    }

    fun onAddTheme(settingId: Int) {
        when (settingId) {
            THEME_USER_CUSTOM_SETTINGS_ID -> openManageCustomThemeLiveData.call()
            else -> {
                // no op
            }
        }
    }

    fun onDeleteTheme(themeId: Int) {
        viewModelScope.launch {
            userThemesRepo.deleteThemesByIds(listOf(themeId.toLong()))
            loadSettingsData()
        }
    }

    fun onEditTheme(themeId: Int) {
        viewModelScope.launch {
            openManageCustomThemeLiveData.value = themeId.toLong()
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

    private fun onThemeChangedInternal(themeId: Int) {
        viewModelScope.launch {
            if (preStoreCustomThemeIfNeeded(themeId)) {
                themesInitializer.reInitializeCustomUserThemes()
            }
            changeThemeLiveData.value = themeId
        }
    }

    private suspend fun preStoreCustomThemeIfNeeded(themeId: Int) = withContext(Dispatchers.IO) {
        val customUserTheme = userThemesRepo.getThemeById(themeId.toLong()) ?: return@withContext false
        userThemesRepo.preStoreThemes(listOf(customUserTheme))
        return@withContext true
    }

    private fun Int.isNativeThemeId() = this == CustomThemeManager.NATIVE_THEME_ID

    companion object {
        private const val CREATE_USER_CUSTOM_THEME_ITEM_ID = -1
        private const val NO_COLOR = -1

        private const val THEME_APP_SETTINGS_ID = 0
        private const val THEME_USER_CUSTOM_SETTINGS_ID = THEME_APP_SETTINGS_ID + 1
        private const val THEME_USE_NATIVE_SETTINGS_ID = THEME_USER_CUSTOM_SETTINGS_ID + 1
        private const val THEME_CHANGE_NATIVE_CONFIG_SETTINGS_ID = THEME_USE_NATIVE_SETTINGS_ID + 1
        private const val THEME_CHANGE_WITH_ANIMATION_SETTINGS_ID = THEME_CHANGE_NATIVE_CONFIG_SETTINGS_ID + 1

        private val CREATE_USER_CUSTOM_THEME_ITEM = CustomTheme.Description(
            id = CREATE_USER_CUSTOM_THEME_ITEM_ID,
            name = CustomTheme.Name.UNDEFINED,
            isLight = false,
            isCustom = false,
            impressColor1 = NO_COLOR,
            impressColor2 = NO_COLOR,
            impressColor3 = NO_COLOR,
        )
    }
}