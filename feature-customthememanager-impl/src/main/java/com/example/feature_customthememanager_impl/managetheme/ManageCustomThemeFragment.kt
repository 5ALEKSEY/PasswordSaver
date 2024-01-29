package com.example.feature_customthememanager_impl.managetheme

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_customthememanager_impl.R
import com.example.feature_customthememanager_impl.base.BaseCustomThemeModuleFragment
import com.example.feature_customthememanager_impl.di.FeatureCustomThemeManagerComponent
import com.example.feature_customthememanager_impl.managetheme.adapter.ThemeModificationRecyclerViewAdapter
import com.example.feature_customthememanager_impl.managetheme.colorpickerdialog.PickColorDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class ManageCustomThemeFragment : BaseCustomThemeModuleFragment<ManageCustomThemeViewModel>() {

    private var fabSaveCustomThemeAction: FloatingActionButton? = null
    private var rvCustomThemeModificationList: RecyclerView? = null
    private var sLightState: SwitchCompat? = null
    private var sSimpleManageEnablingState: SwitchCompat? = null
    private var tietCustomThemeNameField: TextInputEditText? = null
    private var tvSimpleManageDesc: TextView? = null

    private var modificationsAdapter: ThemeModificationRecyclerViewAdapter? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_manage_custom_theme_layout

    override fun createViewModel(): ManageCustomThemeViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun injectFragment(component: FeatureCustomThemeManagerComponent) {
        component.inject(this)
    }

    override fun findViews(fragmentView: View) {
        super.findViews(fragmentView)
        with(fragmentView) {
            fabSaveCustomThemeAction = findViewById(R.id.fabSaveCustomThemeAction)
            rvCustomThemeModificationList = findViewById(R.id.rvCustomThemeModificationList)
            sLightState = findViewById(R.id.sLightState)
            sSimpleManageEnablingState = findViewById(R.id.sSimpleManageEnablingState)
            tietCustomThemeNameField = findViewById(R.id.tietCustomThemeNameField)
            tvSimpleManageDesc = findViewById(R.id.tvSimpleManageDesc)
        }
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
        initSaveCustomThemeButton()
        initSimpleStateChangeButton()
        viewModel.obtainModificationsList(
            arguments?.getInt("themeId", -1)?.toLong(),
            requireContext(),
        )
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.manage_custom_theme_toolbar_title)
            setupBackAction(R.drawable.ic_back_action) {
                navController.popBackStack()
            }
        }
    }

    override fun subscriberToViewModel(viewModel: ManageCustomThemeViewModel) {
        super.subscriberToViewModel(viewModel)
        with(viewModel) {
            subscribeToModificationsList().observe(viewLifecycleOwner) {
                modificationsAdapter?.submitNewItems(it)
            }
            subscribeToModificationsName().observe(viewLifecycleOwner) {
                tietCustomThemeNameField?.setText(it)
            }
            subscribeToLeaveScreen().observe(viewLifecycleOwner) {
                navController.popBackStack()
            }
            subscribeToLightThemeState().observe(viewLifecycleOwner) {
                sLightState?.isChecked = it
            }
        }
    }

    private fun initRecyclerView() {
        modificationsAdapter = ThemeModificationRecyclerViewAdapter(
            this::onPickNewColor,
        )

        rvCustomThemeModificationList?.apply {
            adapter = modificationsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(PsDividerItemDecoration(PsDividerItemDecorationSettings(context)))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        fabSaveCustomThemeAction?.hide()
                    } else {
                        fabSaveCustomThemeAction?.show()
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun initSaveCustomThemeButton() {
        fabSaveCustomThemeAction?.setOnClickListener {
            viewModel.onSaveNewThemeAndLeave(
                tietCustomThemeNameField?.text.toString(),
                sLightState?.isChecked ?: false,
            )
        }
    }

    private fun initSimpleStateChangeButton() {
        sSimpleManageEnablingState?.setOnClickListener {
            viewModel.onSimpleManageStateChanged(sSimpleManageEnablingState?.isChecked ?: false)
        }
    }

    private fun onPickNewColor(itemId: Int, previousColorValue: Int) {
        PickColorDialog.show(childFragmentManager, previousColorValue) {
            viewModel.onChangeColor(itemId, it)
        }
    }
}