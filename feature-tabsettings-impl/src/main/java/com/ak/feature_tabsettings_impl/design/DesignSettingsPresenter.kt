package com.ak.feature_tabsettings_impl.design

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.core_repo_api.intefaces.PasswordShowingType
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class DesignSettingsPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val resourceManager: IResourceManager
) : BasePSPresenter<IDesignSettingsView>() {

    companion object {
        const val SHOWING_TYPE_SETTING_ID = 1
    }

    init {
        FeatureTabSettingsComponent.get().inject(this)
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
                    resourceManager.getString(R.string.showing_type_settings_name),
                    resourceManager.getString(R.string.showing_type_settings_decs),
                    showingType.number,
                    showingTypeList
            )

        viewState.displayAppSettings(listOf(showingTypeSpinnerItem))
    }
}