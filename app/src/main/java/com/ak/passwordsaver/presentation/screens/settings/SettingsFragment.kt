package com.ak.passwordsaver.presentation.screens.settings

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.BasePSFragment
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.spinners.SpinnerSettingsListItemModel
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.switches.SwitchSettingsListItemModel
import com.ak.passwordsaver.utils.bindView
import com.arellomobile.mvp.presenter.InjectPresenter

class SettingsFragment : BasePSFragment(), ISettingsView {

    companion object {
        fun getInstance() = SettingsFragment()
    }

    @InjectPresenter
    lateinit var mSettingsPresenter: SettingsPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_settings_bar)
    private val mSettingsRecyclerView: RecyclerView by bindView(R.id.rv_settings_items_list)

    private lateinit var mSettingsRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        if (activity is AppCompatActivity) {
            val appCompatActivity = activity as AppCompatActivity
            appCompatActivity.setSupportActionBar(mToolbar)
            appCompatActivity.supportActionBar?.title = "Settings"
        }
    }

    private fun initRecyclerView() {
        mSettingsRecyclerAdapter = SettingsRecyclerViewAdapter()
        mSettingsRecyclerView.adapter = mSettingsRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mSettingsRecyclerView.layoutManager = linearLayoutManager
        mSettingsRecyclerView.addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))

        val spinnerList = listOf("Belarus", "Germany", "Russia", "Turkey")

        mSettingsRecyclerAdapter.addSettingsList(
            listOf(
                SwitchSettingsListItemModel("Setting name 1", "Description of setting name 1 ...", true),
                SwitchSettingsListItemModel("Setting name 2", "Description of setting name 2 ...", false),
                SwitchSettingsListItemModel("Setting name 3", "Description of setting name 3 ...", false),
                SwitchSettingsListItemModel("Setting name 4", "Description of setting name 4 ...", true),
                SpinnerSettingsListItemModel("Setting name 4", "Description of setting name 4 ...", 1, spinnerList),
                SwitchSettingsListItemModel("Setting name 5", "Description of setting name 5 ...", true),
                SpinnerSettingsListItemModel("Setting name 5", "Description of setting name 5 ...", 2, spinnerList),
                SwitchSettingsListItemModel("Setting name 6", "Description of setting name 6 ...", false)
            )
        )
    }
}