package com.ak.passwordsaver.presentation.screens.settings.design

import com.ak.domain.data.model.PasswordShowingType
import com.ak.domain.preferences.settings.ISettingsPreferencesManager
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerSettingsListItemModel
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class DesignSettingsPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager
) : BasePSPresenter<IDesignSettingsView>() {

    companion object {
        const val SHOWING_TYPE_SETTING_ID = 1
    }

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
                settingsPreferencesManager.setPasswordShowingType(
                    PasswordShowingType.getTypeByNumber(
                        newDataId
                    )
                )
            }
        }
    }

    private fun loadSettingsData() {
        // Showing Type
        val showingType = settingsPreferencesManager.getPasswordShowingType()
        val showingTypeList = settingsPreferencesManager.getStringListOfPasswordShowingTypes()
        val showingTypeSpinnerItem =
            SpinnerSettingsListItemModel(
                SHOWING_TYPE_SETTING_ID,
                "Showing type",
                "Type of password showing for user",
                showingType.number,
                showingTypeList
            )

        viewState.displayAppSettings(listOf(showingTypeSpinnerItem))
    }
}