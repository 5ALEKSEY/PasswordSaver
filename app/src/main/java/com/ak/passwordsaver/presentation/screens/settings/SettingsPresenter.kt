package com.ak.passwordsaver.presentation.screens.settings

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.R
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.sections.SectionSettingsListItemModel
import com.arellomobile.mvp.InjectViewState
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : BasePSPresenter<ISettingsView>() {

    companion object {
        const val DESIGN_SECTION_SETTING_ID = 1
        const val PRIVACY_SECTION_SETTING_ID = 2
        const val ABOUT_SECTION_SETTING_ID = 3
    }

    @Inject
    lateinit var mSettingsPreferencesManager: SettingsPreferencesManager

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadSettingsData()
    }

    fun onSectionClicked(settingId: Int) {
        when (settingId) {
            DESIGN_SECTION_SETTING_ID -> {
                viewState.showDesignSettings()
            }
            PRIVACY_SECTION_SETTING_ID -> {
                val isAuthEnabled = mSettingsPreferencesManager.isPincodeEnabled()
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

    private fun loadSettingsData() {
        val designSectionItemModel = SectionSettingsListItemModel(
            DESIGN_SECTION_SETTING_ID,
            "Design",
            R.drawable.ic_design_section_action
        )
        val privacySectionItemModel = SectionSettingsListItemModel(
            PRIVACY_SECTION_SETTING_ID,
            "Privacy",
            R.drawable.ic_privacy_section_action
        )
        val aboutSectionItemModel = SectionSettingsListItemModel(
            ABOUT_SECTION_SETTING_ID,
            "About",
            R.drawable.ic_about_section_action
        )

        val sections = listOf(
            designSectionItemModel,
            privacySectionItemModel,
            aboutSectionItemModel
        )
        viewState.displayAppSettings(sections)
    }
}