package com.ak.passwordsaver.presentation.screens.settings.about

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.PSUtils
import com.ak.passwordsaver.utils.bindView
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.arellomobile.mvp.presenter.InjectPresenter

class AboutSettingsActivity : BasePSFragmentActivity(), IAboutSettingsView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AboutSettingsActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var mAboutSettingsPresenter: AboutSettingsPresenter

    private val mToolbar: Toolbar by bindView(R.id.tb_about_settings_bar)
    private val mAppVersionTextView: TextView by bindView(R.id.tv_application_version_info)
    private val mAboutRecyclerView: RecyclerView by bindView(R.id.rv_about_actions_list)
    private val mLauncherImage: ImageView by bindView(R.id.iv_about_launcher_image)

    private lateinit var mAboutRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getScreenLayoutResId() = R.layout.activity_about_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
        displayLauncherImageText("Passwords")
    }

    override fun setVersionInfo(versionInfo: String) {
        mAppVersionTextView.text = versionInfo
    }

    override fun displayAboutActions(settingsItems: List<SettingsListItemModel>) {
        mAboutRecyclerAdapter.addSettingsList(settingsItems)
    }

    override fun startReportAction() {
        showShortTimeMessage("report. aga. shhha")
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        mAboutRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            null,
            mAboutSettingsPresenter::onAboutActionClicked,
            null
        )
        mAboutRecyclerView.apply {
            adapter = mAboutRecyclerAdapter
            val linearLayoutManager = LinearLayoutManager(
                this@AboutSettingsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayoutManager
            addItemDecoration(
                DividerItemDecoration(
                    this@AboutSettingsActivity,
                    linearLayoutManager.orientation
                )
            )
        }
    }

    private fun displayLauncherImageText(text: String) {
        val fillColor = getColorCompat(R.color.colorPrimary)
        val textColor = getColorCompat(R.color.colorWhite)
        val aboutLauncherImageTextSizeInPx = resources.getDimensionPixelSize(R.dimen.about_image_launcher_text_size)
        val aboutLauncherImageSizeInPx = resources.getDimensionPixelSize(R.dimen.about_image_launcher_size)
        mLauncherImage.drawTextInner(
            aboutLauncherImageSizeInPx,
            fillColor,
            textColor,
            aboutLauncherImageTextSizeInPx,
            text
        )
    }
}
