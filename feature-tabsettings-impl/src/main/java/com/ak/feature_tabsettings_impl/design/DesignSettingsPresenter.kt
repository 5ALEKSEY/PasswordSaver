package com.ak.feature_tabsettings_impl.design

import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import javax.inject.Inject
import moxy.InjectViewState

@InjectViewState
class DesignSettingsPresenter @Inject constructor(
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val resourceManager: IResourceManager
) : BasePSPresenter<IDesignSettingsView>() {

    init {
        FeatureTabSettingsComponent.get().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadSettingsData()
    }

    private fun loadSettingsData() {
        // Showing Type
//        val showingType = settingsPreferencesManager.getPasswordShowingType()
//        val showingTypeList = settingsPreferencesManager.getStringListOfPasswordShowingTypes()
//        val showingTypeSpinnerItem =
//            SpinnerSettingsListItemModel(
//                    SHOWING_TYPE_SETTING_ID,
//                    resourceManager.getString(R.string.showing_type_settings_name),
//                    resourceManager.getString(R.string.showing_type_settings_decs),
//                    showingType.number,
//                    showingTypeList
//            )

//        viewState.displayAppSettings(listOf(showingTypeSpinnerItem))
        viewState.displayAppSettings(emptyList())
    }
}