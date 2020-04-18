package com.ak.feature_tabsettings_impl.about

import com.ak.base.presenter.BasePSPresenter
import com.ak.feature_tabsettings_impl.BuildConfig
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AboutSettingsPresenter @Inject constructor() : BasePSPresenter<IAboutSettingsView>() {

    companion object {
        const val REPORT_ABOUT_ACTION_ID = 0
    }

    init {
        FeatureTabSettingsComponent.get().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAboutActions()
        loadApplicationVersion()
    }

    fun onAboutActionClicked(actionId: Int) {
        when (actionId) {
            REPORT_ABOUT_ACTION_ID -> viewState.startReportAction()
        }
    }

    private fun loadAboutActions() {
//        val designSectionItemModel = SectionSettingsListItemModel(
//            REPORT_ABOUT_ACTION_ID,
//            "Report",
//            R.drawable.ic_report_action
//        )
//        viewState.displayAboutActions(listOf(designSectionItemModel))
    }

    private fun loadApplicationVersion() {
        viewState.setVersionInfo(BuildConfig.FULL_VERSION_NAME)
    }
}