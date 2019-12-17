package com.ak.passwordsaver.presentation.screens.settings

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.ui.BasePSFragment
import com.ak.passwordsaver.presentation.screens.auth.SecurityActivity
import com.ak.passwordsaver.presentation.screens.auth.SecurityPresenter
import com.ak.passwordsaver.presentation.screens.settings.about.AboutSettingsActivity
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.design.DesignSettingsActivity
import com.ak.passwordsaver.presentation.screens.settings.privacy.PrivacySettingsActivity
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class SettingsFragment : BasePSFragment(), ISettingsView {

    companion object {
        fun getInstance() = SettingsFragment()
    }

    @InjectPresenter
    lateinit var mSettingsPresenter: SettingsPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_settings_bar)
    private val mSettingsRecyclerView: RecyclerView by bindView(R.id.rv_settings_items_list)

    private lateinit var mSettingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun showDesignSettings() {
        context?.let {
            DesignSettingsActivity.startActivity(it)
        }
    }

    override fun showPrivacySettings() {
        context?.let {
            PrivacySettingsActivity.startActivity(it)
        }
    }

    override fun showAboutScreen() {
        context?.let {
            AboutSettingsActivity.startActivity(it)
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
        mSettingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        if (activity is AppCompatActivity) {
            val appCompatActivity = activity as AppCompatActivity
            appCompatActivity.setSupportActionBar(mToolbar)
            appCompatActivity.supportActionBar?.title = "Settings"
        }
    }

    private fun initRecyclerView() {
        mSettingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            null,
            mSettingsPresenter::onSectionClicked,
            null
        )
        mSettingsRecyclerView.adapter = mSettingsRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mSettingsRecyclerView.layoutManager = linearLayoutManager
        mSettingsRecyclerView.addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
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