package com.ak.passwordsaver.presentation.screens.settings.privacy

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class PrivacySettingsActivity : BasePSFragmentActivity(), IPrivacySettingsView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PrivacySettingsActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mPrivacySettingsPresenter: PrivacySettingsPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_privacy_settings_bar)
    private val mDesignSettingsRecyclerView: RecyclerView by bindView(R.id.rv_privacy_settings_items_list)

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
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Privacy"
        mToolbar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        mSettingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            mPrivacySettingsPresenter::onSwitchSettingsItemChanged,
            mPrivacySettingsPresenter::onSpinnerItemChanged,
            null,
            mPrivacySettingsPresenter::onTextSettingsItemClicked
        )
        mDesignSettingsRecyclerView.apply {
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
        SecurityActivity.startSecurityForResult(this, securityAction)
    }
}
