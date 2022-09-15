package com.ak.feature_tabsettings_impl.design

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.base.BaseSettingsModuleFragment
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_design_settings.view.rvDesignSettingsItemsList

class DesignSettingsFragment : BaseSettingsModuleFragment<DesignSettingsViewModel>() {

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

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
        viewModel.loadSettingsData()
    }

    override fun subscriberToViewModel(viewModel: DesignSettingsViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToDesignSettingsListLiveData().observe(
            viewLifecycleOwner,
            settingsRecyclerAdapter::addSettingsList,
        )
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
}