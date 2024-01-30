package com.ak.feature_tabsettings_impl.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.preference.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.BuildConfig
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.sections.SectionSettingsListItemModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager,
) : BasePSViewModel() {

    companion object {
        const val DEBUG_SECTION_SETTING_ID = 0
        const val DESIGN_SECTION_SETTING_ID = 1
        const val PRIVACY_SECTION_SETTING_ID = 2
        const val BACKUP_SECTION_SETTING_ID = 3
        const val ABOUT_SECTION_SETTING_ID = 4
    }

    private val appSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()
    private val showDesignSettingsLiveData = SingleEventLiveData<Unit?>()
    private val showAuthForPrivacySettingsLiveData = SingleEventLiveData<Unit?>()
    private val showPrivacySettingsLiveData = SingleEventLiveData<Unit?>()
    private val showBackupInfoLiveData = SingleEventLiveData<Unit?>()
    private val showAboutSettingsLiveData = SingleEventLiveData<Unit?>()
    private val showDebugSettingsLiveData = SingleEventLiveData<Unit?>()

    fun subscribeToAppSettingsListLiveData(): LiveData<List<SettingsListItemModel>> = appSettingsListLiveData
    fun subscribeToOpenDesignSettingsLiveData(): LiveData<Unit?> = showDesignSettingsLiveData
    fun subscribeToOpenAuthForPrivacySettingsLiveData(): LiveData<Unit?> = showAuthForPrivacySettingsLiveData
    fun subscribeToOpenPrivacySettingsLiveData(): LiveData<Unit?> = showPrivacySettingsLiveData
    fun subscribeToOpenBackupInfoLiveData(): LiveData<Unit?> = showBackupInfoLiveData
    fun subscribeToOpenAboutSettingsLiveData(): LiveData<Unit?> = showAboutSettingsLiveData
    fun subscribeToOpenDebugSettingsLiveData(): LiveData<Unit?> = showDebugSettingsLiveData

    fun loadSettingsData() {
        val designSectionItemModel = SectionSettingsListItemModel(
            DESIGN_SECTION_SETTING_ID,
            resourceManager.getString(R.string.design_setting_name),
            R.drawable.ic_design_section_action,
            !featuresUpdateManager.isAppThemeFeatureViewed(),
        )
        val privacySectionItemModel = SectionSettingsListItemModel(
            PRIVACY_SECTION_SETTING_ID,
            resourceManager.getString(R.string.privacy_setting_name),
            R.drawable.ic_privacy_section_action,
            !featuresUpdateManager.isFingerprintFeatureViewed(),
        )
        val backupInfoSectionItemModel = SectionSettingsListItemModel(
            BACKUP_SECTION_SETTING_ID,
            resourceManager.getString(R.string.backup_info_name),
            R.drawable.ic_backup_info_section_action,
            !featuresUpdateManager.isBackupFeatureViewed(),
        )
        val aboutSectionItemModel = SectionSettingsListItemModel(
            ABOUT_SECTION_SETTING_ID,
            resourceManager.getString(R.string.about_setting_name),
            R.drawable.ic_about_section_action
        )

        val debugSectionItemModel = if (BuildConfig.DEBUG) {
            SectionSettingsListItemModel(
                DEBUG_SECTION_SETTING_ID,
                resourceManager.getString(R.string.debug_setting_name),
                R.drawable.ic_debug_section_action
            )

        } else {
            null
        }

        appSettingsListLiveData.value = listOfNotNull(
            designSectionItemModel,
            privacySectionItemModel,
            backupInfoSectionItemModel,
            aboutSectionItemModel,
            debugSectionItemModel,
        )
    }

    fun onSectionClicked(settingId: Int) {
        when (settingId) {
            DESIGN_SECTION_SETTING_ID -> showDesignSettingsLiveData.call()
            PRIVACY_SECTION_SETTING_ID -> {
                val isAuthEnabled = settingsPreferencesManager.isPincodeEnabled()
                if (isAuthEnabled) {
                    showAuthForPrivacySettingsLiveData.call()
                } else {
                    showPrivacySettingsLiveData.call()
                }
            }
            BACKUP_SECTION_SETTING_ID -> showBackupInfoLiveData.call()
            ABOUT_SECTION_SETTING_ID -> showAboutSettingsLiveData.call()
            DEBUG_SECTION_SETTING_ID -> showDebugSettingsLiveData.call()
        }
    }
}