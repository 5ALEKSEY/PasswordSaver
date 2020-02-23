package com.ak.passwordsaver.presentation.screens.settings.privacy

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
import kotlinx.android.synthetic.main.fragment_privacy_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class PrivacySettingsFragment : BasePSFragment<PrivacySettingsPresenter>(), IPrivacySettingsView {

    @InjectPresenter
    lateinit var privacySettingsPresenter: PrivacySettingsPresenter

    @ProvidePresenter
    fun providePresenter(): PrivacySettingsPresenter = daggerPresenter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_privacy_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        privacySettingsPresenter.loadSettingsData()
    }

    override fun openAddPincodeScreen() {
        openSecurityScreen(SecurityPresenter.ADD_PINCODE_SECURITY_ACTION_TYPE)
    }

    override fun openChangePincodeScreen() {
        openSecurityScreen(SecurityPresenter.CHANGE_PINCODE_SECURITY_ACTION_TYPE)
    }

    override fun openAddPatternScreen() {
        openSecurityScreen(SecurityPresenter.ADD_PATTERN_SECURITY_ACTION_TYPE)
    }

    override fun openChangePatternScreen() {
        openSecurityScreen(SecurityPresenter.CHANGE_PATTERN_SECURITY_ACTION_TYPE)
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbPrivacySettingsBar)
                supportActionBar?.title = "Privacy"
                tbPrivacySettingsBar.setNavigationOnClickListener {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun initRecyclerView() {
        settingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            privacySettingsPresenter::onSwitchSettingsItemChanged,
            privacySettingsPresenter::onSpinnerItemChanged,
            null,
            privacySettingsPresenter::onTextSettingsItemClicked
        )
        rvPrivacySettingsItemsList.apply {
            adapter = settingsRecyclerAdapter
            val linLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linLayoutManager
            addItemDecoration(DividerItemDecoration(context, linLayoutManager.orientation))
        }
    }

    private fun openSecurityScreen(securityAction: Int) {
        SecurityActivity.startSecurityForResult(
            requireActivity(),
            this,
            securityAction,
            AppConstants.SECURITY_CHANGE_ACTION_REQUEST_CODE
        )
    }
}