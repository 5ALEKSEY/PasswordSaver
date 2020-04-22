package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import android.view.Menu
import android.view.MenuItem
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
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.actionMode.IPasswordsActionModeView
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordsListRecyclerAdapter
import com.ak.feature_tabpasswords_impl.screens.presentation.base.BasePasswordsModuleFragment
import com.ak.feature_tabpasswords_impl.screens.ui.PasswordActionsBottomSheetDialog
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_passwords_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class PasswordsListFragment : BasePasswordsModuleFragment<PasswordsListPresenter>(),
    IPasswordsListView,
    IPasswordsActionModeView {

    @InjectPresenter
    lateinit var passwordsListPresenter: PasswordsListPresenter

    @ProvidePresenter
    fun providePresenter(): PasswordsListPresenter = daggerPresenter

    @InjectPresenter
    lateinit var passwordsActionModePresenter: com.ak.feature_tabpasswords_impl.screens.actionMode.PasswordsActionModePresenter

    @Inject
    lateinit var daggerActionModePresenter: Lazy<com.ak.feature_tabpasswords_impl.screens.actionMode.PasswordsActionModePresenter>

    @ProvidePresenter
    fun provideActionModePresenter(): com.ak.feature_tabpasswords_impl.screens.actionMode.PasswordsActionModePresenter = daggerActionModePresenter.get()

    private var tbPasswordsListBarActionMode: ActionMode? = null
    private var mPasswordActionsDialog: PasswordActionsBottomSheetDialog? = null
    private lateinit var mPasswordsAdapter: PasswordsListRecyclerAdapter

    override fun getFragmentLayoutResId() = R.layout.fragment_passwords_list

    override fun isBackPressEnabled() = false

    override fun injectFragment() {
        FeatureTabPasswordsComponent.get().inject(this)
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()

        fabAddNewPasswordAction.setSafeClickListener {
            navigator.navigateToAddNewPassword()
        }

        passwordsListPresenter.loadPasswords()
    }

    override fun onPause() {
        super.onPause()
        hideSelectedMode()
    }

    override fun displayPasswords(passwordModelsList: List<PasswordItemModel>) {
        mPasswordsAdapter.insertData(passwordModelsList)
    }

    override fun setLoadingState(isLoading: Boolean) {
        passwordsLoadingContainer.setVisibility(isLoading)
        if (isLoading) {
            loadingAnimation.playAnimation()
        } else {
            loadingAnimation.pauseAnimation()
        }
    }

    override fun setEmptyPasswordsState(isEmptyViewVisible: Boolean) {
        incEmptyView.setVisibility(isEmptyViewVisible)
    }

    override fun openPasswordToastMode(passwordName: String, passwordContent: String) {
        val message = "$passwordName: $passwordContent"
        showShortTimeMessage(message)
    }

    override fun setPasswordVisibilityInCardMode(
        passwordId: Long,
        contentVisibilityState: Boolean
    ) {
        mPasswordsAdapter.setPasswordContentVisibility(passwordId, contentVisibilityState)
    }

    override fun enableToolbarScrolling() {
        tbPasswordsListBar.turnOnToolbarScrolling(ablPasswordsListBarLayout)
    }

    override fun disableToolbarScrolling() {
        tbPasswordsListBar.turnOffToolbarScrolling(ablPasswordsListBarLayout)
    }

    override fun showPasswordActionsDialog() {
        mPasswordActionsDialog = PasswordActionsBottomSheetDialog.showDialog(childFragmentManager)
        mPasswordActionsDialog?.onChoosePasswordActionListener = { actionId ->
            when (actionId) {
                PasswordActionsBottomSheetDialog.COPY_PASSWORD_CONTENT_ACTION -> {
                    passwordsListPresenter.onCopyPasswordAction()
                }
                PasswordActionsBottomSheetDialog.EDIT_PASSWORD_ITEM_ACTION -> {
                    passwordsListPresenter.onEditPasswordAction()
                }
                PasswordActionsBottomSheetDialog.DELETE_PASSWORD_ITEM_ACTION -> {
                    passwordsListPresenter.onDeletePasswordAction()
                }
            }
        }
    }

    override fun hidePasswordActionsDialog() {
        mPasswordActionsDialog?.dismiss()
    }

    override fun showEditPasswordScreen(passwordId: Long) {
        navigator.navigateToEditPassword(passwordId)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbPasswordsListBar)
                supportActionBar?.title = "Passwords list"
            }
        }
    }

    private fun initRecyclerView() {
        mPasswordsAdapter = PasswordsListRecyclerAdapter(
            passwordsListPresenter::passwordShowActionRequired,
            passwordsListPresenter::onShowPasswordActions,
            passwordsActionModePresenter::onPasswordItemSingleClick,
            passwordsActionModePresenter::onPasswordItemLongClick
        )

        rvPasswordsList.adapter = mPasswordsAdapter
        rvPasswordsList.layoutManager = GridLayoutManager(
            context,
            AppConstants.PASSWORDS_LIST_COLUMN_COUNT,
            GridLayoutManager.VERTICAL,
            false
        )
        rvPasswordsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fabAddNewPasswordAction.hide()
                } else {
                    fabAddNewPasswordAction.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
        (rvPasswordsList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    //------------------------------------- Action mode --------------------------------------------

    override fun showSelectedItemsQuantityText(text: String) {
        tbPasswordsListBarActionMode?.title = text
    }

    override fun displaySelectedMode() {
        val activityOfFragment = activity
        mPasswordsAdapter.setItemsActionModeState(true)
        if (tbPasswordsListBarActionMode == null && activityOfFragment != null && activityOfFragment is AppCompatActivity) {
            tbPasswordsListBarActionMode =
                activityOfFragment.startSupportActionMode(object : ActionMode.Callback {
                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        return when (item?.itemId) {
                            R.id.action_delete_selected_passwords -> {
                                passwordsActionModePresenter.onDeleteSelectedInActionMode()
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
        mPasswordsAdapter.setItemsActionModeState(false)
        passwordsActionModePresenter.onSelectedModeFinished()
        tbPasswordsListBarActionMode?.finish()
        tbPasswordsListBarActionMode = null
    }

    override fun showSelectStateForItem(isSelected: Boolean, passwordId: Long) {
        mPasswordsAdapter.setSelectedStateForPasswordItemId(isSelected, passwordId)
    }
}