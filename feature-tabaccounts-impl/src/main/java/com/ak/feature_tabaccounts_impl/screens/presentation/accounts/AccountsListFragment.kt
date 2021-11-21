package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.fragment_accounts_list.view.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountsListPresenter.loadPasswords()
    }

    override fun initViewBeforePresenterAttach(fragmentView: View) {
        super.initViewBeforePresenterAttach(fragmentView)
        initToolbar()
        initRecyclerView()

        fragmentView.fabAddNewAccountAction.setSafeClickListener {
            navigator.navigateToAddNewAccount()
        }
    }

    override fun onResume() {
        super.onResume()
        deleteAccountDialog?.dismissAllowingStateLoss()
    }

    override fun onPause() {
        super.onPause()
        hideSelectedMode()
    }

    override fun displayAccounts(accountModelsList: List<AccountItemModel>) {
        accountsAdapter.insertData(accountModelsList)
    }

    override fun setLoadingState(isLoading: Boolean) {
        fragmentView.accountLoadingContainer.setVisibility(isLoading)
        if (isLoading) {
            fragmentView.loadingAnimation.playAnimation()
        } else {
            fragmentView.loadingAnimation.pauseAnimation()
        }
    }

    override fun setEmptyAccountsState(isEmptyViewVisible: Boolean) {
        fragmentView.incEmptyView.setVisibility(isEmptyViewVisible)
    }

    override fun enableToolbarScrolling() {
        fragmentView.tbAccountsListBar.turnOnToolbarScrolling(ablAccountsListBarLayout)
    }

    override fun disableToolbarScrolling() {
        fragmentView.tbAccountsListBar.turnOffToolbarScrolling(ablAccountsListBarLayout)
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
                setSupportActionBar(fragmentView.tbAccountsListBar)
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

        with(fragmentView.rvAccountsList) {
            adapter = accountsAdapter
            layoutManager = GridLayoutManager(
                    context,
                    AppConstants.PASSWORDS_LIST_COLUMN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        fragmentView.fabAddNewAccountAction.hide()
                    } else {
                        fragmentView.fabAddNewAccountAction.show()
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    //------------------------------------- Action mode --------------------------------------------

    override fun showSelectedItemsQuantityText(text: String) {
        tbAccountsListBarActionMode?.title = text
    }

    override fun displaySelectedMode() {
        val activityOfFragment = requireActivity()
        accountsAdapter.setItemsActionModeState(true)
        if (tbAccountsListBarActionMode == null && activityOfFragment is AppCompatActivity) {
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

    @SuppressLint("StringFormatInvalid")
    private inline fun showDeleteAccountDialog(crossinline deleteCallback: () -> Unit) {
        deleteAccountDialog?.dismissAllowingStateLoss()
        val deleteItemString = getString(R.string.delete_dialog_account_item)
        deleteAccountDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.delete_data_dialog_title))
            .description(getString(R.string.delete_data_dialog_desc, deleteItemString))
            .positive(getString(R.string.delete_dialog_pst_text)) {
                deleteAccountDialog?.dismissAllowingStateLoss()
                deleteCallback()
            }
            .cancelable(false)
            .buildAndShow()
    }
}