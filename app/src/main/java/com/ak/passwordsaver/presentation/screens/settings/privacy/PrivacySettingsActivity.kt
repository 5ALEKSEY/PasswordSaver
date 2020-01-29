package com.ak.passwordsaver.presentation.screens.settings.privacy

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.activity_privacy_settings.*
import moxy.presenter.InjectPresenter

class PrivacySettingsActivity : BasePSFragmentActivity<PrivacySettingsPresenter>(),
    IPrivacySettingsView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PrivacySettingsActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mPrivacySettingsPresenter: PrivacySettingsPresenter

    private lateinit var mSettingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getScreenLayoutResId() = R.layout.activity_privacy_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        mPrivacySettingsPresenter.loadSettingsData()
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
        mSettingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        setSupportActionBar(tbPrivacySettingsBar)
        supportActionBar?.title = "Privacy"
        tbPrivacySettingsBar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        mSettingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            mPrivacySettingsPresenter::onSwitchSettingsItemChanged,
            mPrivacySettingsPresenter::onSpinnerItemChanged,
            null,
            mPrivacySettingsPresenter::onTextSettingsItemClicked
        )
        rvPrivacySettingsItemsList.apply {
            adapter = mSettingsRecyclerAdapter
            val linLayoutManager = LinearLayoutManager(
                this@PrivacySettingsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linLayoutManager
            addItemDecoration(DividerItemDecoration(context, linLayoutManager.orientation))
        }
    }

    private fun openSecurityScreen(securityAction: Int) {
        SecurityActivity.startSecurityForResult(
            this,
            securityAction,
            AppConstants.SECURITY_CHANGE_ACTION_REQUEST_CODE
        )
    }
}
