package com.ak.passwordsaver.presentation.screens.settings.privacy

import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.texts.TextSettingsListItemModel
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class PrivacySettingsPresenter : BasePSPresenter<IPrivacySettingsView>() {

    companion object {
        private const val PINCODE_ENABLE_SETTINGS_ID = 1
        private const val PINCODE_CHANGE_SETTINGS_ID = 2
        private const val PATTERN_ENABLE_SETTINGS_ID = 4
        private const val PATTERN_CHANGE_SETTINGS_ID = 5
    }

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun loadSettingsData() {
        // Pincode
        val pincodeSwitchItemModel = SwitchSettingsListItemModel(
            PINCODE_ENABLE_SETTINGS_ID,
            "Pincode",
            "If you turn on this option, you can use privacy check before open your password content",
            false
        )
        val pincodeChangeTextItemModel = TextSettingsListItemModel(
            PINCODE_CHANGE_SETTINGS_ID,
            "Change pincode"
        )

        // Pattern
        val patternSwitchItemModel = SwitchSettingsListItemModel(
            PATTERN_ENABLE_SETTINGS_ID,
            "Pattern",
            "You can use graphic pattern code for check your password content",
            false
        )
        val patternChangeTextItemModel = TextSettingsListItemModel(
            PATTERN_CHANGE_SETTINGS_ID,
            "Change your pattern"
        )

        val items = listOf(
            pincodeSwitchItemModel,
            pincodeChangeTextItemModel,
            patternSwitchItemModel,
            patternChangeTextItemModel
        )
        viewState.displayAppSettings(items)
    }
}