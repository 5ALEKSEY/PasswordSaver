package com.ak.feature_tabsettings_impl.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabsettings_impl.BuildConfig
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import javax.inject.Inject

class AboutSettingsViewModel @Inject constructor() : BasePSViewModel() {

    companion object {
        const val REPORT_ABOUT_ACTION_ID = 0
    }

    private val startReportActionLiveData = SingleEventLiveData<Unit?>()
    private val versionInfoLiveData = MutableLiveData<String>()
    private val aboutActionsLiveData = MutableLiveData<List<SettingsListItemModel>>()

    fun subscribeToStartReportLiveData(): LiveData<Unit?> = startReportActionLiveData
    fun subscribeToVersionInfoLiveData(): LiveData<String> = versionInfoLiveData
    fun subscribeToAboutActionsLiveData(): LiveData<List<SettingsListItemModel>> = aboutActionsLiveData

    fun onAboutActionClicked(actionId: Int) {
        when (actionId) {
            REPORT_ABOUT_ACTION_ID -> startReportActionLiveData.call()
        }
    }

    fun onInitSettings() {
        loadAboutActions()
        loadApplicationVersion()
    }

    private fun loadAboutActions() {

    }

    private fun loadApplicationVersion() {
        versionInfoLiveData.value = BuildConfig.FULL_VERSION_NAME
    }
}