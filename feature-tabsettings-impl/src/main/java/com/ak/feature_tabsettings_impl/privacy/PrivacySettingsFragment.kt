package com.ak.feature_tabsettings_impl.privacy

import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
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
import kotlinx.android.synthetic.main.fragment_privacy_settings.view.rvPrivacySettingsItemsList

class PrivacySettingsFragment : BaseSettingsModuleFragment<PrivacySettingsViewModel>() {

    @Inject
    protected lateinit var authChecker: IAuthCheckerStarter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter
    private var routeToSecureSettingsDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_privacy_settings

    override fun injectFragment(component: FeatureTabSettingsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): PrivacySettingsViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
    }

    override fun subscriberToViewModel(viewModel: PrivacySettingsViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToPrivacySettingsListLiveData().observe(viewLifecycleOwner, this::displayPrivacySettings)
        viewModel.subscribeToShowAddPincodeLiveData().observe(viewLifecycleOwner) {
            openSecurityScreen(IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE)
        }
        viewModel.subscribeToShowEditPincodeLiveData().observe(viewLifecycleOwner) {
            openSecurityScreen(IAuthCheckerStarter.CHANGE_PINCODE_SECURITY_ACTION_TYPE)
        }
        viewModel.subscribeToShowAddPatternLiveData().observe(viewLifecycleOwner) {
            openSecurityScreen(IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE)
        }
        viewModel.subscribeToShowEditPatternLiveData().observe(viewLifecycleOwner) {
            openSecurityScreen(IAuthCheckerStarter.CHANGE_PATTERN_SECURITY_ACTION_TYPE)
        }
        viewModel.subscribeToShowAddNewFingerprintLiveData().observe(viewLifecycleOwner) {
            showAddNewFingerprintDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        routeToSecureSettingsDialog?.dismissAllowingStateLoss()
        viewModel.loadSettingsData()
    }

    private fun showAddNewFingerprintDialog() {
        routeToSecureSettingsDialog?.dismissAllowingStateLoss()
        routeToSecureSettingsDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.no_added_fingerprints_dialog_title))
            .description(getString(R.string.no_added_fingerprints_dialog_desc))
            .positive(getString(R.string.no_added_fingerprints_dialog_pos_btn)) {
                startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS).also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
            }
            .dismissDialogListener { viewModel.loadSettingsData() }
            .cancelable(false)
            .buildAndShow()
    }

    private fun displayPrivacySettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.privacy_settings_toolbar_title)
            setupBackAction(R.drawable.ic_back_action) {
                navController.popBackStack()
            }
        }
    }

    private fun initRecyclerView() {
        settingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            onSwitchSettingsChanged = viewModel::onSwitchSettingsItemChanged,
            onSpinnerSettingsChanged = viewModel::onSpinnerItemChanged,
            onTextSettingsClicked = viewModel::onTextSettingsItemClicked
        )
        fragmentView.rvPrivacySettingsItemsList.apply {
            adapter = settingsRecyclerAdapter
            val linLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linLayoutManager
            addItemDecoration(PsDividerItemDecoration(PsDividerItemDecorationSettings(context)))
        }
    }

    private fun openSecurityScreen(securityAction: Int) {
        activity?.let {
            authChecker.startAuthChange(
                    it,
                    this,
                    securityAction
            )
        }
    }
}