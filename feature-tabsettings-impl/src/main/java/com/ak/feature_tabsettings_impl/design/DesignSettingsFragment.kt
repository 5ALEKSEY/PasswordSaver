package com.ak.feature_tabsettings_impl.design

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.base.BaseSettingsModuleFragment
import com.ak.feature_tabsettings_impl.design.configurenative.ConfigureNativeThemeDialog
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_design_settings.view.rvDesignSettingsItemsList

class DesignSettingsFragment : BaseSettingsModuleFragment<DesignSettingsViewModel>() {

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    private var nativeThemeConfigAttentionDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_design_settings

    override fun injectFragment(component: FeatureTabSettingsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): DesignSettingsViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        nativeThemeConfigAttentionDialog?.dismissAllowingStateLoss()
        viewModel.loadSettingsData()
    }

    override fun subscriberToViewModel(viewModel: DesignSettingsViewModel) {
        super.subscriberToViewModel(viewModel)
        with(viewModel) {
            subscribeToDesignSettingsListLiveData().observe(
                viewLifecycleOwner,
                settingsRecyclerAdapter::addSettingsList,
            )
            subscribeToChangeThemeLiveData().observe(viewLifecycleOwner) {
                CustomThemeManager.getInstance().setTheme(it, requireContext())
            }
            subscribeToOpenNativeThemeConfigDialogLiveData().observe(viewLifecycleOwner) {
                showNativeThemeConfigDialog(it)
            }
        }
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.design_settings_toolbar_title)
            setupBackAction(R.drawable.ic_back_action) {
                navController.popBackStack()
            }
        }
    }

    private fun initRecyclerView() {
        settingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            onThemeChanged = viewModel::onThemeChanged,
            onTextSettingsClicked = viewModel::onSettingTextItemClicked,
            onSwitchSettingsChanged = viewModel::onSwitchSettingsItemChanged,
        )

        fragmentView.rvDesignSettingsItemsList.apply {
            adapter = settingsRecyclerAdapter
            val linLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false,
            )
            layoutManager = linLayoutManager
            addItemDecoration(PsDividerItemDecoration(PsDividerItemDecorationSettings(context)))
        }
    }

    private fun showNativeThemeConfigDialog(shouldShowAttentionDialog: Boolean) {
        nativeThemeConfigAttentionDialog?.dismissAllowingStateLoss()

        if (!shouldShowAttentionDialog) {
            showNativeThemeConfigDialog()
            return
        }

        nativeThemeConfigAttentionDialog = PSDialogBuilder(childFragmentManager)
            .cancelable(false)
            .title(getString(R.string.native_theme_config_attention_popup_title))
            .description(getString(R.string.native_theme_config_attention_popup_desc))
            .positive(getString(R.string.native_theme_config_attention_popup_positive)) {
                nativeThemeConfigAttentionDialog?.dismissAllowingStateLoss()
                showNativeThemeConfigDialog()
            }
            .negative(getString(R.string.native_theme_config_attention_popup_negative)) {
                nativeThemeConfigAttentionDialog?.dismissAllowingStateLoss()
                viewModel.onNativeThemeConfigurationSkipped()
            }
            .buildAndShow()
    }

    private fun showNativeThemeConfigDialog() {
        val nativeThemeConfigListener = object : ConfigureNativeThemeDialog.Listener {
            override fun onConfigApplied(lightThemeId: Int, darkThemeId: Int) {
                viewModel.onNativeThemeConfigured(lightThemeId, darkThemeId)
            }

            override fun onConfigCancelled() {
                viewModel.onNativeThemeConfigurationSkipped()
            }
        }

        ConfigureNativeThemeDialog.show(childFragmentManager, nativeThemeConfigListener)
    }
}