package com.ak.feature_tabsettings_impl.about

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.base.extensions.drawTextInner
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.base.BaseSettingsModuleFragment
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import de.hdodenhof.circleimageview.CircleImageView

class AboutSettingsFragment : BaseSettingsModuleFragment<AboutSettingsViewModel>() {

    private var ivAboutLauncherImage: CircleImageView? = null
    private var rvAboutActionsList: RecyclerView? = null
    private var tvApplicationVersionInfo: TextView? = null

    private lateinit var aboutRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_about_settings

    override fun injectFragment(component: FeatureTabSettingsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): AboutSettingsViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun findViews(fragmentView: View) {
        super.findViews(fragmentView)
        with(fragmentView) {
            ivAboutLauncherImage = findViewById(R.id.ivAboutLauncherImage)
            rvAboutActionsList = findViewById(R.id.rvAboutActionsList)
            tvApplicationVersionInfo = findViewById(R.id.tvApplicationVersionInfo)
        }
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
        displayLauncherImageText(getString(R.string.about_launch_image_text))
        viewModel.onInitSettings()
    }

    override fun subscriberToViewModel(viewModel: AboutSettingsViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToStartReportLiveData().observe(viewLifecycleOwner) {
            startReportAction()
        }
        viewModel.subscribeToVersionInfoLiveData().observe(viewLifecycleOwner, this::setVersionInfo)
        viewModel.subscribeToAboutActionsLiveData().observe(viewLifecycleOwner, this::displayAboutActions)
    }

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        displayLauncherImageText(getString(R.string.about_launch_image_text), theme)
    }

    private fun setVersionInfo(versionInfo: String) {
        tvApplicationVersionInfo?.text = versionInfo
    }

    private fun displayAboutActions(settingsItems: List<SettingsListItemModel>) {
        aboutRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun startReportAction() {
//        showShortTimeMessage("report. aga. shhha")
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.about_settings_toolbar_title)
            setupBackAction(R.drawable.ic_back_action) {
                navController.popBackStack()
            }
        }
    }

    private fun initRecyclerView() {
        aboutRecyclerAdapter = SettingsRecyclerViewAdapter(
            onSwitchSettingsChanged = { a, f -> },
            onSectionSettingsClicked = viewModel::onAboutActionClicked,
            onTextSettingsClicked = { d -> }
        )
        rvAboutActionsList?.apply {
            adapter = aboutRecyclerAdapter
            val linearLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayoutManager
            addItemDecoration(PsDividerItemDecoration(PsDividerItemDecorationSettings(context)))
        }
    }

    private fun displayLauncherImageText(text: String, theme: CustomTheme = CustomThemeManager.getCurrentAppliedTheme()) {
        val fillColor = theme.getColor(R.attr.themedPrimaryColor)
        val textColor = theme.getColor(R.attr.staticColorWhite)
        val aboutLauncherImageTextSizeInPx =
            resources.getDimensionPixelSize(R.dimen.about_image_launcher_text_size)
        val aboutLauncherImageSizeInPx =
            resources.getDimensionPixelSize(R.dimen.about_image_launcher_size)
        ivAboutLauncherImage?.apply {
            drawTextInner(
                requireContext(),
                aboutLauncherImageSizeInPx,
                fillColor,
                textColor,
                aboutLauncherImageTextSizeInPx,
                text
            )
            borderColor = theme.getColor(R.attr.themedPrimaryDarkColor)
        }
    }
}