package com.ak.feature_tabsettings_impl.debug

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabsettings_impl.BuildConfig
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.base.BaseSettingsModuleFragment
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_debug_settings.view.rvDebugSettingsItemsList

class DebugSettingsFragment : BaseSettingsModuleFragment<DebugSettingsViewModel>() {

    private lateinit var debugRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_debug_settings

    override fun injectFragment(component: FeatureTabSettingsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): DebugSettingsViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) {
            navController.popBackStack()
        }
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
    }

    override fun subscriberToViewModel(viewModel: DebugSettingsViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToDebugSettingsList().observe(
            viewLifecycleOwner,
            debugRecyclerAdapter::addSettingsList,
        )
        viewModel.subscribeToSwitchToNextTheme().observe(viewLifecycleOwner) { enable ->
            if (enable) {
                DebugNextThemeSwitcher.startNextThemeSwitching(requireContext())
            } else {
                DebugNextThemeSwitcher.stopNextThemeSwitching()
            }
            viewModel.loadDebugSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDebugSettings()
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.debug_settings_toolbar_title)
            setupBackAction(R.drawable.ic_back_action) {
                navController.popBackStack()
            }
        }
    }

    private fun initRecyclerView() {
        debugRecyclerAdapter = SettingsRecyclerViewAdapter(
            onSwitchSettingsChanged = viewModel::onSwitchSettingsItemChanged,
            onTextSettingsClicked = viewModel::onSettingTextItemClicked,
        )
        fragmentView.rvDebugSettingsItemsList.apply {
            adapter = debugRecyclerAdapter
            val linearLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayoutManager
            addItemDecoration(PsDividerItemDecoration(PsDividerItemDecorationSettings(context)))
        }
    }
}