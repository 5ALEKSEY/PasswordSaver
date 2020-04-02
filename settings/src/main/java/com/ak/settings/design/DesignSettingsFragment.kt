package com.ak.settings.design

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.ui.BasePSFragment
import com.ak.settings.R
import com.ak.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.fragment_design_settings.*
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

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbDesignSettingsBar)
                supportActionBar?.title = "Design"
                tbDesignSettingsBar.setNavigationOnClickListener {
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
        rvDesignSettingsItemsList.apply {
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