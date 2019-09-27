package com.ak.passwordsaver.presentation.screens.settings

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.PasswordShowingType
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel
import com.arellomobile.mvp.InjectViewState
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : BasePSPresenter<ISettingsView>() {

    companion object {
        const val SHOWING_TYPE_SETTING_ID = 1
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

    fun settingSpinnerItemChanged(settingId: Int, newDataId: Int) {
        when (settingId) {
            SHOWING_TYPE_SETTING_ID -> {
                mSettingsPreferencesManager.setPasswordShowingType(PasswordShowingType.getTypeByNumber(newDataId))
            }
        }
    }

    private fun loadSettingsData() {
        // ------------------------------------------ Showing Type -----------------------------------------------------
        val showingType = mSettingsPreferencesManager.getPasswordShowingType()
        val showingTypeList = mSettingsPreferencesManager.getStringListOfPasswordShowingTypes()
        val showingTypeSpinnerItem = SpinnerSettingsListItemModel(
            SHOWING_TYPE_SETTING_ID,
            "Showing type",
            "Type of password showing for user",
            showingType.number,
            showingTypeList
        )
        // -------------------------------------------------------------------------------------------------------------

        viewState.displayAppSettings(listOf(showingTypeSpinnerItem))
    }
}