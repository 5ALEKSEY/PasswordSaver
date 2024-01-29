package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.airbnb.lottie.LottieAnimationView
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordsListClickListener
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordsListRecyclerAdapter
import com.ak.feature_tabpasswords_impl.screens.presentation.base.BasePasswordsModuleFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PasswordsListFragment : BasePasswordsModuleFragment<PasswordsListViewModel>() {

    private var fabAddNewPasswordAction: FloatingActionButton? = null
    private var incEmptyView: View? = null
    private var loadingAnimation: LottieAnimationView? = null
    private var passwordsLoadingContainer: LinearLayout? = null
    private var rvPasswordsList: RecyclerView? = null

    private lateinit var passwordsActionModeViewModel: PasswordsActionModeViewModel

    override fun createViewModel(): PasswordsListViewModel {
        return injectViewModel(viewModelsFactory)
    }

    private val passwordsListClickListener = object : PasswordsListClickListener {
        override fun selectPasswordItem(item: PasswordItemModel) {
            passwordsActionModeViewModel.onPasswordItemSelect(item.passwordId)
        }

        override fun showPasswordItemContent(item: PasswordItemModel) {
            passwordsAdapter.setPasswordContentVisibility(item.passwordId, !item.isPasswordContentVisible)
        }

        override fun copyPasswordItemContent(item: PasswordItemModel) {
            viewModel.onCopyPasswordAction(item.passwordId)
        }

        override fun editPasswordItem(item: PasswordItemModel) {
            viewModel.onEditPasswordAction(item.passwordId)
        }

        override fun deletePasswordItem(item: PasswordItemModel) {
            showDeletePasswordDialog {
                viewModel.onDeletePasswordAction(item.passwordId)
            }
        }

        override fun pinPasswordItem(item: PasswordItemModel) {
            viewModel.pinPassword(item.passwordId)
        }

        override fun unpinPasswordItem(item: PasswordItemModel) {
            viewModel.unpinPassword(item.passwordId)
        }

        override fun onShowPopupMenu(item: PasswordItemModel) {
            passwordsAdapter.setContextMenuOpenedForPasswordItem(item.passwordId)
        }

        override fun onDismissPopupmenu(item: PasswordItemModel) {
            passwordsAdapter.clearContextMenuOpenedForPasswordItems()
        }
    }

    private var tbPasswordsListBarActionMode: ActionMode? = null
    private lateinit var passwordsAdapter: PasswordsListRecyclerAdapter

    private var deletePasswordDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_passwords_list

    override fun isBackPressEnabled() = false

    override fun injectFragment(component: FeatureTabPasswordsComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passwordsActionModeViewModel = injectViewModel(viewModelsFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPasswords()
    }

    override fun findViews(fragmentView: View) {
        super.findViews(fragmentView)
        with(fragmentView) {
            fabAddNewPasswordAction = findViewById(R.id.fabAddNewPasswordAction)
            incEmptyView = findViewById(R.id.incEmptyView)
            loadingAnimation = findViewById(R.id.loadingAnimation)
            passwordsLoadingContainer = findViewById(R.id.passwordsLoadingContainer)
            rvPasswordsList = findViewById(R.id.rvPasswordsList)
        }
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()

        fabAddNewPasswordAction?.setSafeClickListener {
            navigator.navigateToAddNewPassword()
        }
    }

    override fun subscriberToViewModel(viewModel: PasswordsListViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeEmptyPasswordState().observe(viewLifecycleOwner) {
            incEmptyView?.setVisibility(it)
        }
        viewModel.subscribeToPrimaryLoadingState().observe(viewLifecycleOwner) { isLoading ->
            passwordsLoadingContainer?.setVisibility(isLoading)
            if (isLoading) {
                loadingAnimation?.playAnimation()
            } else {
                loadingAnimation?.pauseAnimation()
            }
        }
        viewModel.subscribeToSecondaryLoadingState().observe(viewLifecycleOwner, this::setSecondaryLoadingState)
        viewModel.subscribeToShowEditPasswordScreen().observe(viewLifecycleOwner) { passwordId ->
            navigator.navigateToEditPassword(passwordId)
        }
        viewModel.subscribeToToolbarScrollingState().observe(viewLifecycleOwner) { canScrollToolbar ->
        }
        viewModel.subscribeToPasswordsList().observe(viewLifecycleOwner) {
            passwordsAdapter.insertData(it)
        }

        passwordsActionModeViewModel.subscribeToSelectedModeState().observe(viewLifecycleOwner) { hasSelectedMode ->
            if (hasSelectedMode) displaySelectedMode() else hideSelectedMode()
        }
        passwordsActionModeViewModel.subscribeToSelectedStateForItem().observe(viewLifecycleOwner) { (isSelected, passwordId) ->
            showSelectStateForItem(isSelected, passwordId)
        }
        passwordsActionModeViewModel.subscribeToSelectedItemsQuantityText().observe(viewLifecycleOwner) { text ->
            showSelectedItemsQuantityText(text)
        }
    }

    override fun onResume() {
        super.onResume()
        deletePasswordDialog?.dismissAllowingStateLoss()
    }

    override fun onPause() {
        super.onPause()
        hideSelectedMode()
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.passwords_list_toolbar_title)
        }
    }

    private fun initRecyclerView() {
        passwordsAdapter = PasswordsListRecyclerAdapter(passwordsListClickListener)

        rvPasswordsList?.apply {
            adapter = passwordsAdapter
            layoutManager = GridLayoutManager(
                context,
                AppConstants.PASSWORDS_LIST_COLUMN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        fabAddNewPasswordAction?.hide()
                    } else {
                        fabAddNewPasswordAction?.show()
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })

            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun setSecondaryLoadingState(isLoading: Boolean) {
        applyForToolbarController {
            if (isLoading) {
                startToolbarTitleLoading(R.string.toolbar_title_loading_updating_text)
            } else {
                stopToolbarTitleLoading()
                initToolbar()
            }
        }
    }

    //------------------------------------- Action mode --------------------------------------------

    private fun showSelectedItemsQuantityText(text: String) {
        tbPasswordsListBarActionMode?.title = text
    }

    private fun displaySelectedMode() {
        val activityOfFragment = requireActivity()
        passwordsAdapter.setItemsActionModeState(true)
        if (tbPasswordsListBarActionMode == null && activityOfFragment is AppCompatActivity) {
            tbPasswordsListBarActionMode =
                activityOfFragment.startSupportActionMode(object : ActionMode.Callback {
                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        return when (item?.itemId) {
                            R.id.action_delete_selected_passwords -> {
                                showDeletePasswordDialog { passwordsActionModeViewModel.onDeleteSelectedInActionMode() }
                                true
                            }
                            else -> false
                        }
                    }

                    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        mode?.menuInflater?.inflate(R.menu.passwords_list_action_mode_menu, menu)
                        return true
                    }

                    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                        return false
                    }

                    override fun onDestroyActionMode(p0: ActionMode?) {
                        hideSelectedMode()
                    }
                })
        }
    }

    private fun hideSelectedMode() {
        passwordsAdapter.setItemsActionModeState(false)
        passwordsActionModeViewModel.onSelectedModeFinished()
        tbPasswordsListBarActionMode?.finish()
        tbPasswordsListBarActionMode = null
    }

    private fun showSelectStateForItem(isSelected: Boolean, passwordId: Long) {
        passwordsAdapter.setSelectedStateForPasswordItemId(isSelected, passwordId)
    }

    private inline fun showDeletePasswordDialog(crossinline deleteCallback: () -> Unit) {
        deletePasswordDialog?.dismissAllowingStateLoss()
        val deleteItemString = getString(R.string.delete_dialog_password_item)
        deletePasswordDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.dialog_attention_title))
            .description(getString(R.string.delete_data_dialog_desc, deleteItemString))
            .positive(getString(R.string.delete_dialog_pst_text)) {
                deletePasswordDialog?.dismissAllowingStateLoss()
                deleteCallback()
            }
            .cancelable(false)
            .buildAndShow()
    }
}