package com.ak.passwordsaver.presentation.screens.settings

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragment
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.fragment_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class SettingsFragment : BasePSFragment<SettingsPresenter>(), ISettingsView {

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    @ProvidePresenter
    fun providePresenter(): SettingsPresenter = daggerPresenter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_settings

    override fun isBackPressEnabled() = false

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()
        settingsPresenter.loadSettingsData()
    }

    override fun showDesignSettings() {
        context?.let {
            navController.navigate(R.id.action_settingsFragment_to_designSettingsFragment)
        }
    }

    override fun showPrivacySettings() {
        context?.let {
            navController.navigate(R.id.action_settingsFragment_to_privacySettingsFragment)
        }
    }

    override fun showAboutScreen() {
        context?.let {
            navController.navigate(R.id.action_settingsFragment_to_aboutSettingsFragment)
        }
    }

    override fun startAuthAndOpenPrivacySettings() {
        activity?.let {
            SecurityActivity.startSecurityForResult(
                it,
                this,
                SecurityPresenter.AUTH_SECURITY_ACTION_TYPE,
                AppConstants.SECURITY_AUTH_ACTION_REQUEST_CODE
            )
        }
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbSettingsBar)
                supportActionBar?.title = "Settings"
            }
        }
    }

    private fun initRecyclerView() {
        settingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            null,
            settingsPresenter::onSectionClicked,
            null
        )
        rvSettingsItemsList.adapter = settingsRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSettingsItemsList.layoutManager = linearLayoutManager
        rvSettingsItemsList.addItemDecoration(
            DividerItemDecoration(
                context,
                linearLayoutManager.orientation
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.SECURITY_AUTH_ACTION_REQUEST_CODE -> handleSecurityAuthResult(resultCode)
        }
    }

    private fun handleSecurityAuthResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            showPrivacySettings()
        } else {
            showShortTimeMessage("Is it honestly you??? :)")
        }
    }
}