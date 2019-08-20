package com.ak.passwordsaver.presentation.screens.settings

import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.BasePSFragment

class SettingsFragment : BasePSFragment() {

    companion object {
        fun getInstance() = SettingsFragment()
    }

    override fun getFragmentLayoutResId() = R.layout.fragment_settings
}