package com.ak.feature_tabsettings_impl.about

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.base.extensions.drawTextInner
import com.ak.base.extensions.getColorCompat
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabsettings_impl.R
import com.ak.feature_tabsettings_impl.adapter.SettingsRecyclerViewAdapter
import com.ak.feature_tabsettings_impl.adapter.items.SettingsListItemModel
import com.ak.feature_tabsettings_impl.base.BaseSettingsModuleFragment
import com.ak.feature_tabsettings_impl.di.FeatureTabSettingsComponent
import kotlinx.android.synthetic.main.fragment_about_settings.view.ivAboutLauncherImage
import kotlinx.android.synthetic.main.fragment_about_settings.view.rvAboutActionsList
import kotlinx.android.synthetic.main.fragment_about_settings.view.tbAboutSettingsBar
import kotlinx.android.synthetic.main.fragment_about_settings.view.tvApplicationVersionInfo

class AboutSettingsFragment : BaseSettingsModuleFragment<AboutSettingsViewModel>() {

    private lateinit var aboutRecyclerAdapter: SettingsRecyclerViewAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_about_settings

    override fun injectFragment(component: FeatureTabSettingsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): AboutSettingsViewModel {
        return injectViewModel(viewModelsFactory)
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

    private fun setVersionInfo(versionInfo: String) {
        fragmentView.tvApplicationVersionInfo.text = versionInfo
    }

    private fun displayAboutActions(settingsItems: List<SettingsListItemModel>) {
        aboutRecyclerAdapter.addSettingsList(settingsItems)
    }

    private fun startReportAction() {
//        showShortTimeMessage("report. aga. shhha")
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                val actionBaeView = fragmentView.tbAboutSettingsBar
                setSupportActionBar(actionBaeView)
                actionBaeView.setNavigationOnClickListener {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun initRecyclerView() {
        aboutRecyclerAdapter = SettingsRecyclerViewAdapter(
            null,
            null,
            viewModel::onAboutActionClicked,
            null
        )
        fragmentView.rvAboutActionsList.apply {
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
        val textColor = getColorCompat(R.color.staticColorWhite)
        val aboutLauncherImageTextSizeInPx =
            resources.getDimensionPixelSize(R.dimen.about_image_launcher_text_size)
        val aboutLauncherImageSizeInPx =
            resources.getDimensionPixelSize(R.dimen.about_image_launcher_size)
        fragmentView.ivAboutLauncherImage.drawTextInner(
            requireContext(),
            aboutLauncherImageSizeInPx,
            fillColor,
            textColor,
            aboutLauncherImageTextSizeInPx,
            text
        )
    }
}