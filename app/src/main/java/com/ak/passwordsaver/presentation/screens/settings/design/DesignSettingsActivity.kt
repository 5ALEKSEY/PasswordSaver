package com.ak.passwordsaver.presentation.screens.settings.design

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import kotlinx.android.synthetic.main.activity_design_settings.*
import moxy.presenter.InjectPresenter

class DesignSettingsActivity : BasePSFragmentActivity<DesignSettingsPresenter>(),
    IDesignSettingsView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DesignSettingsActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mDesignSettingsPresenter: DesignSettingsPresenter

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
        setSupportActionBar(tbDesignSettingsBar)
        supportActionBar?.title = "Design"
        tbDesignSettingsBar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        mSettingsRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            mDesignSettingsPresenter::settingSpinnerItemChanged,
            null,
            null
        )
        rvDesignSettingsItemsList.apply {
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
