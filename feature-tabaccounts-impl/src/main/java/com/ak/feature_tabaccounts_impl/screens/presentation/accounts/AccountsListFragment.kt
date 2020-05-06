package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

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
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.actionMode.AccountsActionModePresenter
import com.ak.feature_tabaccounts_impl.screens.actionMode.IAccountsActionModeView
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountsListRecyclerAdapter
import com.ak.feature_tabaccounts_impl.screens.presentation.base.BaseAccountsModuleFragment
import com.ak.feature_tabaccounts_impl.screens.presentation.ui.AccountActionsBottomSheetDialog
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_accounts_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AccountsListFragment : BaseAccountsModuleFragment<AccountsListPresenter>(),
    IAccountsListView,
    IAccountsActionModeView {

    @InjectPresenter
    lateinit var accountsListPresenter: AccountsListPresenter

    @ProvidePresenter
    fun providePresenter(): AccountsListPresenter = daggerPresenter

    @InjectPresenter
    lateinit var accountsActionModePresenter: AccountsActionModePresenter

    @Inject
    lateinit var daggerActionModePresenter: Lazy<AccountsActionModePresenter>

    @ProvidePresenter
    fun provideActionModePresenter(): AccountsActionModePresenter = daggerActionModePresenter.get()

    private var tbAccountsListBarActionMode: ActionMode? = null
    private var accountActionsDialog: AccountActionsBottomSheetDialog? = null
    private lateinit var accountsAdapter: AccountsListRecyclerAdapter

    private var deleteAccountDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_accounts_list

    override fun isBackPressEnabled() = false

    override fun injectFragment() {
        FeatureTabAccountsComponent.get().inject(this)
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()

        fabAddNewAccountAction.setSafeClickListener {
            navigator.navigateToAddNewAccount()
        }

        deleteAccountDialog?.dismissAllowingStateLoss()
        accountsListPresenter.loadPasswords()
    }

    override fun onPause() {
        super.onPause()
        hideSelectedMode()
    }

    override fun displayAccounts(accountModelsList: List<AccountItemModel>) {
        accountsAdapter.insertData(accountModelsList)
    }

    override fun setLoadingState(isLoading: Boolean) {
        accountLoadingContainer.setVisibility(isLoading)
        if (isLoading) {
            loadingAnimation.playAnimation()
        } else {
            loadingAnimation.pauseAnimation()
        }
    }

    override fun setEmptyAccountsState(isEmptyViewVisible: Boolean) {
        incEmptyView.setVisibility(isEmptyViewVisible)
    }

    override fun enableToolbarScrolling() {
        tbAccountsListBar.turnOnToolbarScrolling(ablAccountsListBarLayout)
    }

    override fun disableToolbarScrolling() {
        tbAccountsListBar.turnOffToolbarScrolling(ablAccountsListBarLayout)
    }

    override fun showAccountActionsDialog() {
        accountActionsDialog = AccountActionsBottomSheetDialog.showDialog(childFragmentManager)
        accountActionsDialog?.onChooseAccountActionListener = { actionId ->
            when (actionId) {
                AccountActionsBottomSheetDialog.COPY_ACCOUNT_LOGIN_ACTION -> {
                    accountsListPresenter.onCopyAccountLoginAction()
                }
                AccountActionsBottomSheetDialog.COPY_ACCOUNT_PASSWORD_ACTION -> {
                    accountsListPresenter.onCopyAccountPasswordAction()
                }
                AccountActionsBottomSheetDialog.EDIT_ACCOUNT_ITEM_ACTION -> {
                    accountsListPresenter.onEditAccountAction()
                }
                AccountActionsBottomSheetDialog.DELETE_ACCOUNT_ITEM_ACTION -> {
                    showDeleteAccountDialog { accountsListPresenter.onDeleteAccountAction() }
                }
            }
        }
    }

    override fun hideAccountActionsDialog() {
        accountActionsDialog?.dismiss()
    }

    override fun showEditAccountScreen(accountId: Long) {
        navigator.navigateToEditAccount(accountId)
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbAccountsListBar)
                supportActionBar?.title = getString(R.string.accounts_list_toolbar_title)
            }
        }
    }

    private fun initRecyclerView() {
        accountsAdapter = AccountsListRecyclerAdapter(
                accountsListPresenter::onShowAccountActions,
                accountsActionModePresenter::onPasswordItemSingleClick,
                accountsActionModePresenter::onPasswordItemLongClick
        )

        rvAccountsList.adapter = accountsAdapter
        rvAccountsList.layoutManager = GridLayoutManager(
                context,
                AppConstants.PASSWORDS_LIST_COLUMN_COUNT,
                GridLayoutManager.VERTICAL,
                false
        )
        rvAccountsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fabAddNewAccountAction.hide()
                } else {
                    fabAddNewAccountAction.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
        (rvAccountsList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    //------------------------------------- Action mode --------------------------------------------

    override fun showSelectedItemsQuantityText(text: String) {
        tbAccountsListBarActionMode?.title = text
    }

    override fun displaySelectedMode() {
        val activityOfFragment = activity
        accountsAdapter.setItemsActionModeState(true)
        if (tbAccountsListBarActionMode == null && activityOfFragment != null && activityOfFragment is AppCompatActivity) {
            tbAccountsListBarActionMode =
                activityOfFragment.startSupportActionMode(object : ActionMode.Callback {
                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        return when (item?.itemId) {
                            R.id.action_delete_selected_accounts -> {
                                showDeleteAccountDialog { accountsActionModePresenter.onDeleteSelectedInActionMode() }
                                true
                            }
                            else -> false
                        }
                    }

                    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        mode?.menuInflater?.inflate(R.menu.accounts_list_action_mode_menu, menu)
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
        accountsAdapter.setItemsActionModeState(false)
        accountsActionModePresenter.onSelectedModeFinished()
        tbAccountsListBarActionMode?.finish()
        tbAccountsListBarActionMode = null
    }

    override fun showSelectStateForItem(isSelected: Boolean, accountId: Long) {
        accountsAdapter.setSelectedStateForAccountItemId(isSelected, accountId)
    }

    private inline fun showDeleteAccountDialog(crossinline deleteCallback: () -> Unit) {
        deleteAccountDialog?.dismissAllowingStateLoss()
        val deleteItem = getString(R.string.delete_dialog_account_item)
        deleteAccountDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.delete_data_dialog_title))
            .description(getString(R.string.delete_data_dialog_desc, deleteItem))
            .positive(getString(R.string.delete_dialog_pst_text)) {
                deleteAccountDialog?.dismissAllowingStateLoss()
                deleteCallback()
            }
            .cancelable(false)
            .buildAndShow()
    }
}