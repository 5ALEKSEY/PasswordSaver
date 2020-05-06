package com.ak.feature_tabsettings_impl.privacy

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.BasePSFragment
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.feature_security_api.interfaces.IAuthCheckerStarter
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_privacy_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class PrivacySettingsFragment : BasePSFragment<PrivacySettingsPresenter>(),
    IPrivacySettingsView {

    @Inject
    lateinit var authChecker: IAuthCheckerStarter

    @InjectPresenter
    lateinit var privacySettingsPresenter: PrivacySettingsPresenter

    @ProvidePresenter
    fun providePresenter(): PrivacySettingsPresenter = daggerPresenter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter
    private var routeToSecureSettingsDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_privacy_settings

    override fun injectFragment() {
        FeatureTabSettingsComponent.get().inject(this)
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        routeToSecureSettingsDialog?.dismissAllowingStateLoss()
        privacySettingsPresenter.loadSettingsData()
    }

    override fun openAddPincodeScreen() {
        openSecurityScreen(IAuthCheckerStarter.ADD_PINCODE_SECURITY_ACTION_TYPE)
    }

    override fun openChangePincodeScreen() {
        openSecurityScreen(IAuthCheckerStarter.CHANGE_PINCODE_SECURITY_ACTION_TYPE)
    }

    override fun openAddPatternScreen() {
        openSecurityScreen(IAuthCheckerStarter.ADD_PATTERN_SECURITY_ACTION_TYPE)
    }

    override fun openChangePatternScreen() {
        openSecurityScreen(IAuthCheckerStarter.CHANGE_PATTERN_SECURITY_ACTION_TYPE)
    }

    override fun showAddNewFingerprintDialog() {
        routeToSecureSettingsDialog?.dismissAllowingStateLoss()
        routeToSecureSettingsDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.no_added_fingerprints_dialog_title))
            .description(getString(R.string.no_added_fingerprints_dialog_desc))
            .positive(getString(R.string.no_added_fingerprints_dialog_pos_btn)) {
                startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS).also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
            }
            .dismissDialogListener { privacySettingsPresenter.loadSettingsData() }
            .cancelable(false)
            .buildAndShow()
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbPrivacySettingsBar)
                supportActionBar?.title = getString(R.string.privacy_settings_toolbar_title)
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
        activity?.let {
            authChecker.startAuthChange(
                    it,
                    this,
                    securityAction
            )
        }
    }
}