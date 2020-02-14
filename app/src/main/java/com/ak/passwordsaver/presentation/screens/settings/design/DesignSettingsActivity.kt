package com.ak.passwordsaver.presentation.screens.settings.design

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.activity_design_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class DesignSettingsActivity : BasePSFragmentActivity<DesignSettingsPresenter>(),
    IDesignSettingsView {

    @InjectPresenter
    lateinit var designSettingsPresenter: DesignSettingsPresenter

    @ProvidePresenter
    fun providePresenter(): DesignSettingsPresenter = daggerPresenter

    private lateinit var settingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getScreenLayoutResId() = R.layout.activity_design_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        settingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        setSupportActionBar(tbDesignSettingsBar)
        supportActionBar?.title = "Design"
        tbDesignSettingsBar.setNavigationOnClickListener { finish() }
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
                this@DesignSettingsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linLayoutManager
            addItemDecoration(DividerItemDecoration(context, linLayoutManager.orientation))
        }
    }
}
