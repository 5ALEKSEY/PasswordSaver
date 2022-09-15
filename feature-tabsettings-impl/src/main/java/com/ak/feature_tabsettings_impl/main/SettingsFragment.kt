package com.ak.feature_tabsettings_impl.main

import android.content.Intent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.extensions.showToastMessage
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.base.BaseSettingsModuleFragment
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_settings.view.rvSettingsItemsList

class SettingsFragment : BaseSettingsModuleFragment<SettingsViewModel>() {

    @Inject
    protected lateinit var authChecker: IAuthCheckerStarter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_settings

    override fun isBackPressEnabled() = false

    override fun injectFragment(component: FeatureTabSettingsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): SettingsViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
    }

    override fun subscriberToViewModel(viewModel: SettingsViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToOpenDesignSettingsLiveData().observe(viewLifecycleOwner) { showDesignSettings() }
        viewModel.subscribeToOpenPrivacySettingsLiveData().observe(viewLifecycleOwner) { showPrivacySettings() }
        viewModel.subscribeToOpenAboutSettingsLiveData().observe(viewLifecycleOwner) { showAboutScreen() }
        viewModel.subscribeToOpenAuthForPrivacySettingsLiveData().observe(viewLifecycleOwner) { startAuthAndOpenPrivacySettings() }
        viewModel.subscribeToAppSettingsListLiveData().observe(viewLifecycleOwner, this::displayAppSettings)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSettingsData()
    }

    private fun showDesignSettings() {
        context?.let {
            navController.navigate(R.id.action_settingsFragment_to_designSettingsFragment)
        }
    }

    private fun showPrivacySettings() {
        context?.let {
            navController.navigate(R.id.action_settingsFragment_to_privacySettingsFragment)
        }
    }

    private fun showAboutScreen() {
        context?.let {
            navController.navigate(R.id.action_settingsFragment_to_aboutSettingsFragment)
        }
    }

    private fun startAuthAndOpenPrivacySettings() {
        activity?.let {
            authChecker.startAuthCheck(
                it,
                this,
                IAuthCheckerStarter.AUTH_SECURITY_ACTION_TYPE,
                object : IAuthCheckerStarter.CheckAuthCallback {
                    override fun onAuthSuccessfully() {
                        postponedEventManager.postponeOrInvokeFor(Lifecycle.Event.ON_RESUME) {
                            showPrivacySettings()
                        }
                    }

                    override fun onAuthFailed() {
                        showToastMessage(getString(R.string.auth_failed_funny_text))
                    }
                }
            )
        }
    }

    private fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.settings_toolbar_title)
        }
    }

    private fun initRecyclerView() {
        settingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            onSectionSettingsClicked = viewModel::onSectionClicked,
        )

        with(fragmentView.rvSettingsItemsList) {
            adapter = settingsRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                PsDividerItemDecoration(
                    PsDividerItemDecorationSettings(
                        context = context,
                        offsetDp = PsDividerItemDecorationSettings.Offset(left = 48F),
                    )
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authChecker.handleActivityResult(requestCode, resultCode)
    }
}