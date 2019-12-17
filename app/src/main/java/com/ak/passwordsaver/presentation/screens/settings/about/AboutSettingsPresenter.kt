package com.ak.passwordsaver.presentation.screens.settings.about

import com.ak.passwordsaver.BuildConfig
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.sections.SectionSettingsListItemModel
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class AboutSettingsPresenter : BasePSPresenter<IAboutSettingsView>() {

    companion object {
        const val REPORT_ABOUT_ACTION_ID = 0
    }

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAboutActions()
        loadApplicationVersion()
    }

    fun onAboutActionClicked(actionId: Int)  {
        when (actionId) {
            REPORT_ABOUT_ACTION_ID -> viewState.startReportAction()
        }
    }

    private fun loadAboutActions() {
        val designSectionItemModel = SectionSettingsListItemModel(
            REPORT_ABOUT_ACTION_ID,
            "Report",
            R.drawable.ic_report_action
        )
        viewState.displayAboutActions(listOf(designSectionItemModel))
    }

    private fun loadApplicationVersion() {
        viewState.setVersionInfo(BuildConfig.FULL_VERSION_NAME)
    }
}