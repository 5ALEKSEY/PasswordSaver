package com.ak.passwordsaver.presentation.screens.settings

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class SettingsPresenter : BasePSPresenter<ISettingsView>() {

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }
}