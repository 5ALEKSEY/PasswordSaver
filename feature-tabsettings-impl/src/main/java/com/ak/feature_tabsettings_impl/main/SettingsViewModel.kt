package com.ak.feature_tabsettings_impl.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.sections.SectionSettingsListItemModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager
): BasePSViewModel() {

    companion object {
        const val DESIGN_SECTION_SETTING_ID = 1
        const val PRIVACY_SECTION_SETTING_ID = 2
        const val ABOUT_SECTION_SETTING_ID = 3
    }

    private val appSettingsListLiveData = MutableLiveData<List<SettingsListItemModel>>()
    private val showDesignSettingsLiveData = SingleEventLiveData<Unit?>()
    private val showAuthForPrivacySettingsLiveData = SingleEventLiveData<Unit?>()
    private val showPrivacySettingsLiveData = SingleEventLiveData<Unit?>()
    private val showAboutSettingsLiveData = SingleEventLiveData<Unit?>()

    fun subscribeToAppSettingsListLiveData(): LiveData<List<SettingsListItemModel>> = appSettingsListLiveData
    fun subscribeToOpenDesignSettingsLiveData(): LiveData<Unit?> = showDesignSettingsLiveData
    fun subscribeToOpenAuthForPrivacySettingsLiveData(): LiveData<Unit?> = showAuthForPrivacySettingsLiveData
    fun subscribeToOpenPrivacySettingsLiveData(): LiveData<Unit?> = showPrivacySettingsLiveData
    fun subscribeToOpenAboutSettingsLiveData(): LiveData<Unit?> = showAboutSettingsLiveData

    fun loadSettingsData() {
        val designSectionItemModel = SectionSettingsListItemModel(
            DESIGN_SECTION_SETTING_ID,
            resourceManager.getString(R.string.design_setting_name),
            R.drawable.ic_design_section_action
        )
        val privacySectionItemModel = SectionSettingsListItemModel(
            PRIVACY_SECTION_SETTING_ID,
            resourceManager.getString(R.string.privacy_setting_name),
            R.drawable.ic_privacy_section_action,
            isPrivacySectionHasNewBadge()
        )
        val aboutSectionItemModel = SectionSettingsListItemModel(
            ABOUT_SECTION_SETTING_ID,
            resourceManager.getString(R.string.about_setting_name),
            R.drawable.ic_about_section_action
        )

        val sections = listOf(
            designSectionItemModel,
            privacySectionItemModel,
            aboutSectionItemModel
        )
        appSettingsListLiveData.value = sections
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
            ABOUT_SECTION_SETTING_ID -> showAboutSettingsLiveData.call()
        }
    }

    private fun isPrivacySectionHasNewBadge() = !featuresUpdateManager.isFingerprintFeatureViewed()
}