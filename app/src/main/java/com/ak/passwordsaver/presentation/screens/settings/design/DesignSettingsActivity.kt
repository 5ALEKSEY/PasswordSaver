package com.ak.passwordsaver.presentation.screens.settings.design

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class DesignSettingsActivity : BasePSFragmentActivity(), IDesignSettingsView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DesignSettingsActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mDesignSettingsPresenter: DesignSettingsPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_design_settings_bar)
    private val mDesignSettingsRecyclerView: RecyclerView by bindView(R.id.rv_design_settings_items_list)

    private lateinit var mSettingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getScreenLayoutResId() = R.layout.activity_design_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    override fun displayAppSettings(settingsItems: List<SettingsListItemModel>) {
        mSettingsRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Design"
        mToolbar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        mSettingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            mDesignSettingsPresenter::settingSpinnerItemChanged,
            null
        )
        mDesignSettingsRecyclerView.apply {
            adapter = mSettingsRecyclerAdapter
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
