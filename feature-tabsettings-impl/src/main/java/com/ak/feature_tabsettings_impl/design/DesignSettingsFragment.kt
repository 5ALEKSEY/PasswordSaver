package com.ak.feature_tabsettings_impl.design

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.BasePSFragment
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_design_settings.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class DesignSettingsFragment : BasePSFragment<DesignSettingsPresenter>(),
    IDesignSettingsView {

    @InjectPresenter
    lateinit var designSettingsPresenter: DesignSettingsPresenter

    @ProvidePresenter
    fun providePresenter(): DesignSettingsPresenter = daggerPresenter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_design_settings

    override fun injectFragment() {
        FeatureTabSettingsComponent.get().inject(this)
    }

    override fun initViewBeforePresenterAttach(fragmentView: View) {
        super.initViewBeforePresenterAttach(fragmentView)
        initToolbar()
        initRecyclerView()
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                val actionBarView = fragmentView.tbDesignSettingsBar
                setSupportActionBar(actionBarView)
                supportActionBar?.title = getString(R.string.design_settings_toolbar_title)
                actionBarView.setNavigationOnClickListener {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun initRecyclerView() {
        settingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            designSettingsPresenter::settingSpinnerItemChanged,
            null,
            null
        )

        fragmentView.rvDesignSettingsItemsList.apply {
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
}