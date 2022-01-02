package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.base.extensions.turnOffToolbarScrolling
import com.ak.base.extensions.turnOnToolbarScrolling
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.actionMode.IPasswordsActionModeView
import com.ak.feature_tabpasswords_impl.screens.actionMode.PasswordsActionModePresenter
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordsListRecyclerAdapter
import com.ak.feature_tabpasswords_impl.screens.presentation.base.BasePasswordsModuleFragment
import com.ak.feature_tabpasswords_impl.screens.ui.PasswordActionsBottomSheetDialog
import dagger.Lazy
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_passwords_list.ablPasswordsListBarLayout
import kotlinx.android.synthetic.main.fragment_passwords_list.view.fabAddNewPasswordAction
import kotlinx.android.synthetic.main.fragment_passwords_list.view.incEmptyView
import kotlinx.android.synthetic.main.fragment_passwords_list.view.loadingAnimation
import kotlinx.android.synthetic.main.fragment_passwords_list.view.passwordsLoadingContainer
import kotlinx.android.synthetic.main.fragment_passwords_list.view.rvPasswordsList
import kotlinx.android.synthetic.main.fragment_passwords_list.view.tbPasswordsListBar
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class PasswordsListFragment : BasePasswordsModuleFragment<PasswordsListPresenter>(),
    IPasswordsListView,
    IPasswordsActionModeView {

    @InjectPresenter
    lateinit var passwordsListPresenter: PasswordsListPresenter

    @ProvidePresenter
    fun providePresenter(): PasswordsListPresenter = daggerPresenter

    @InjectPresenter
    lateinit var passwordsActionModePresenter: PasswordsActionModePresenter

    @Inject
    lateinit var daggerActionModePresenter: Lazy<PasswordsActionModePresenter>

    @ProvidePresenter
    fun provideActionModePresenter(): PasswordsActionModePresenter = daggerActionModePresenter.get()

    private var tbPasswordsListBarActionMode: ActionMode? = null
    private var passwordActionsDialog: PasswordActionsBottomSheetDialog? = null
    private lateinit var passwordsAdapter: PasswordsListRecyclerAdapter

    private var deletePasswordDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_passwords_list

    override fun isBackPressEnabled() = false

    override fun injectFragment() {
        FeatureTabPasswordsComponent.get().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordsListPresenter.loadPasswords()
    }

    override fun initViewBeforePresenterAttach(fragmentView: View) {
        super.initViewBeforePresenterAttach(fragmentView)
        initToolbar()
        initRecyclerView()

        fragmentView.fabAddNewPasswordAction.setSafeClickListener {
            navigator.navigateToAddNewPassword()
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

    override fun displayPasswords(passwordModelsList: List<PasswordItemModel>) {
        passwordsAdapter.insertData(passwordModelsList)
    }

    override fun setLoadingState(isLoading: Boolean) {
        fragmentView.passwordsLoadingContainer.setVisibility(isLoading)
        if (isLoading) {
            fragmentView.loadingAnimation.playAnimation()
        } else {
            fragmentView.loadingAnimation.pauseAnimation()
        }
    }

    override fun setEmptyPasswordsState(isEmptyViewVisible: Boolean) {
        fragmentView.incEmptyView.setVisibility(isEmptyViewVisible)
    }

    override fun setPasswordContentVisibility(
        passwordId: Long,
        contentVisibilityState: Boolean
    ) {
        passwordsAdapter.setPasswordContentVisibility(passwordId, contentVisibilityState)
    }

    override fun enableToolbarScrolling() {
        fragmentView.tbPasswordsListBar.turnOnToolbarScrolling(ablPasswordsListBarLayout)
    }

    override fun disableToolbarScrolling() {
        fragmentView.tbPasswordsListBar.turnOffToolbarScrolling(ablPasswordsListBarLayout)
    }

    override fun showPasswordActionsDialog(isContentVisible: Boolean) {
        passwordActionsDialog = PasswordActionsBottomSheetDialog.showDialog(
            childFragmentManager,
            isContentVisible
        )
        passwordActionsDialog?.onChoosePasswordActionListener = { actionId ->
            when (actionId) {
                PasswordActionsBottomSheetDialog.SHOW_PASSWORD_CONTENT_ACTION -> {
                    passwordsListPresenter.onShowPasswordAction()
                }
                PasswordActionsBottomSheetDialog.HIDE_PASSWORD_CONTENT_ACTION -> {
                    passwordsListPresenter.onHidePasswordAction()
                }
                PasswordActionsBottomSheetDialog.COPY_PASSWORD_CONTENT_ACTION -> {
                    passwordsListPresenter.onCopyPasswordAction()
                }
                PasswordActionsBottomSheetDialog.EDIT_PASSWORD_ITEM_ACTION -> {
                    passwordsListPresenter.onEditPasswordAction()
                }
                PasswordActionsBottomSheetDialog.DELETE_PASSWORD_ITEM_ACTION -> {
                    showDeletePasswordDialog { passwordsListPresenter.onDeletePasswordAction() }
                }
            }
        }
    }

    override fun hidePasswordActionsDialog() {
        passwordActionsDialog?.dismiss()
    }

    override fun showEditPasswordScreen(passwordId: Long) {
        navigator.navigateToEditPassword(passwordId)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(fragmentView.tbPasswordsListBar)
                supportActionBar?.title = getString(R.string.passwords_list_toolbar_title)
            }
        }
    }

    private fun initRecyclerView() {
        passwordsAdapter = PasswordsListRecyclerAdapter(
            passwordsListPresenter::onShowPasswordActions,
            passwordsActionModePresenter::onPasswordItemSingleClick,
            passwordsActionModePresenter::onPasswordItemLongClick
        )

        with(fragmentView.rvPasswordsList) {
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
                        fragmentView.fabAddNewPasswordAction.hide()
                    } else {
                        fragmentView.fabAddNewPasswordAction.show()
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })

            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    //------------------------------------- Action mode --------------------------------------------

    override fun showSelectedItemsQuantityText(text: String) {
        tbPasswordsListBarActionMode?.title = text
    }

    override fun displaySelectedMode() {
        val activityOfFragment = requireActivity()
        passwordsAdapter.setItemsActionModeState(true)
        if (tbPasswordsListBarActionMode == null && activityOfFragment is AppCompatActivity) {
            tbPasswordsListBarActionMode =
                activityOfFragment.startSupportActionMode(object : ActionMode.Callback {
                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        return when (item?.itemId) {
                            R.id.action_delete_selected_passwords -> {
                                showDeletePasswordDialog { passwordsActionModePresenter.onDeleteSelectedInActionMode() }
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

    override fun hideSelectedMode() {
        passwordsAdapter.setItemsActionModeState(false)
        passwordsActionModePresenter.onSelectedModeFinished()
        tbPasswordsListBarActionMode?.finish()
        tbPasswordsListBarActionMode = null
    }

    override fun showSelectStateForItem(isSelected: Boolean, passwordId: Long) {
        passwordsAdapter.setSelectedStateForPasswordItemId(isSelected, passwordId)
    }

    private inline fun showDeletePasswordDialog(crossinline deleteCallback: () -> Unit) {
        deletePasswordDialog?.dismissAllowingStateLoss()
        val deleteItemString = getString(R.string.delete_dialog_password_item)
        deletePasswordDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.delete_data_dialog_title))
            .description(getString(R.string.delete_data_dialog_desc, deleteItemString))
            .positive(getString(R.string.delete_dialog_pst_text)) {
                deletePasswordDialog?.dismissAllowingStateLoss()
                deleteCallback()
            }
            .cancelable(false)
            .buildAndShow()
    }
}