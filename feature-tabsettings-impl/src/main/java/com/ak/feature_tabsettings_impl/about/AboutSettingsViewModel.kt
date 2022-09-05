package com.ak.feature_tabsettings_impl.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabsettings_impl.BuildConfig
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.sections.SectionSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.feature_tabsettings_impl.adapter.items.texts.TextSettingsListItemModel
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
        val designSectionItemModel = SectionSettingsListItemModel(
            REPORT_ABOUT_ACTION_ID,
            "Report",
            R.drawable.ic_report_action
        )
        val a = listOf(
            designSectionItemModel,
            SwitchSettingsListItemModel(
                2,
                "Name 1",
                "Description 1",
                false,
                true,
            ),
            TextSettingsListItemModel(4, "Name text"),
            SwitchSettingsListItemModel(
                3,
                "Name 2",
                "Description 2",
                false,
                false,
            ),
            SwitchSettingsListItemModel(
                3,
                "Name 3",
                "Description 3",
                true,
                false,
            )
        )
        aboutActionsLiveData.value = a
    }

    private fun loadApplicationVersion() {
        versionInfoLiveData.value = BuildConfig.FULL_VERSION_NAME
    }
}