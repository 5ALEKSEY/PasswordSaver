package com.ak.settings.about

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.passwordsaver.R
import com.ak.passwordsaver.presentation.base.ui.BasePSFragment
import com.ak.settings.adapter.SettingsRecyclerViewAdapter
import com.ak.settings.adapter.items.SettingsListItemModel
import com.ak.passwordsaver.utils.extensions.drawTextInner
import com.ak.passwordsaver.utils.extensions.getColorCompat
import kotlinx.android.synthetic.main.fragment_about_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class AboutSettingsFragment : BasePSFragment<AboutSettingsPresenter>(),
    IAboutSettingsView {

    @InjectPresenter
    lateinit var aboutSettingsPresenter: AboutSettingsPresenter

    @ProvidePresenter
    fun providePresenter(): AboutSettingsPresenter = daggerPresenter

    private lateinit var aboutRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_about_settings

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
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbAboutSettingsBar)
                tbAboutSettingsBar.setNavigationOnClickListener {
                    navController.popBackStack()
                }
            }
        }
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
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
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