package com.ak.feature_tabsettings_impl.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.BasePSFragment
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SettingsFragment : BasePSFragment<SettingsPresenter>(),
    ISettingsView {

    @Inject
    protected lateinit var authChecker: IAuthCheckerStarter

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    @ProvidePresenter
    fun providePresenter(): SettingsPresenter = daggerPresenter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_settings

    override fun isBackPressEnabled() = false

    override fun injectFragment() {
        FeatureTabSettingsComponent.get().inject(this)
    }

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
            authChecker.startAuthCheck(
                    it,
                    this,
                    IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE,
                    object : IAuthCheckerStarter.CheckAuthCallback {
                        override fun onAuthSuccessfully() {
                            showPrivacySettings()
                        }

                        override fun onAuthFailed() {
                            showShortTimeMessage("Is it honestly you??? :)")
                        }
                    }
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
        authChecker.handleActivityResult(requestCode, resultCode)
    }
}