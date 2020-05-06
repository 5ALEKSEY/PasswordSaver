package com.ak.feature_tabsettings_impl.main

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.sections.SectionSettingsListItemModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
    private val resourceManager: IResourceManager
) : BasePSPresenter<ISettingsView>() {

    companion object {
        const val DESIGN_SECTION_SETTING_ID = 1
        const val PRIVACY_SECTION_SETTING_ID = 2
        const val ABOUT_SECTION_SETTING_ID = 3
    }

    init {
        FeatureTabSettingsComponent.get().inject(this)
    }

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
        viewState.displayAppSettings(sections)
    }

    fun onSectionClicked(settingId: Int) {
        when (settingId) {
            DESIGN_SECTION_SETTING_ID -> {
                viewState.showDesignSettings()
            }
            PRIVACY_SECTION_SETTING_ID -> {
                val isAuthEnabled = settingsPreferencesManager.isPincodeEnabled()
                if (isAuthEnabled) {
                    viewState.startAuthAndOpenPrivacySettings()
                } else {
                    viewState.showPrivacySettings()
                }
            }
            ABOUT_SECTION_SETTING_ID -> {
                viewState.showAboutScreen()
            }
        }
    }

    private fun isPrivacySectionHasNewBadge() = !featuresUpdateManager.isFingerprintFeatureViewed()
}