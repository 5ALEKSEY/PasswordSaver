package com.ak.passwordsaver.presentation.screens.settings.about

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragmentActivity
import com.ak.passwordsaver.presentation.screens.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.passwordsaver.presentation.screens.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.getColorCompat
import kotlinx.android.synthetic.main.activity_about_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class AboutSettingsActivity : BasePSFragmentActivity<AboutSettingsPresenter>(), IAboutSettingsView {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AboutSettingsActivity::class.java))
        }
    }

    @InjectPresenter
    lateinit var aboutSettingsPresenter: AboutSettingsPresenter

    @ProvidePresenter
    fun providePresenter(): AboutSettingsPresenter = daggerPresenter

    private lateinit var aboutRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getScreenLayoutResId() = R.layout.activity_about_settings

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()
        initRecyclerView()
        displayLauncherImageText("Passwords")
    }

    override fun setVersionInfo(versionInfo: String) {
        tvApplicationVersionInfo.text = versionInfo
    }

    override fun displayAboutActions(settingsItems: List<SettingsListItemModel>) {
        aboutRecyclerAdapter.addSettingsList(settingsItems)
    }

    override fun startReportAction() {
        showShortTimeMessage("report. aga. shhha")
    }

    private fun initToolbar() {
        setSupportActionBar(tbAboutSettingsBar)
        tbAboutSettingsBar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        aboutRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            null,
            aboutSettingsPresenter::onAboutActionClicked,
            null
        )
        rvAboutActionsList.apply {
            adapter = aboutRecyclerAdapter
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
        val aboutLauncherImageTextSizeInPx =
            resources.getDimensionPixelSize(R.dimen.about_image_launcher_text_size)
        val aboutLauncherImageSizeInPx =
            resources.getDimensionPixelSize(R.dimen.about_image_launcher_size)
        ivAboutLauncherImage.drawTextInner(
            aboutLauncherImageSizeInPx,
            fillColor,
            textColor,
            aboutLauncherImageTextSizeInPx,
            text
        )
    }
}
