package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.extensions.setVisibility
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountListClickListener
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountsListRecyclerAdapter
import com.ak.feature_tabaccounts_impl.screens.presentation.base.BaseAccountsModuleFragment
import kotlinx.android.synthetic.main.fragment_accounts_list.view.accountLoadingContainer
import kotlinx.android.synthetic.main.fragment_accounts_list.view.fabAddNewAccountAction
import kotlinx.android.synthetic.main.fragment_accounts_list.view.incAccountsEmptyView
import kotlinx.android.synthetic.main.fragment_accounts_list.view.loadingAnimation
import kotlinx.android.synthetic.main.fragment_accounts_list.view.rvAccountsList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AccountsListFragment : BaseAccountsModuleFragment<AccountsListViewModel>() {

    private lateinit var accountsActionModeViewModel: AccountsActionModeViewModel

    private val accountsListClickListener = object : AccountListClickListener {
        override fun selectAccountItem(item: AccountItemModel) {
            accountsActionModeViewModel.onAccountItemSelect(item.accountId)
        }

        override fun showAccountItemContent(item: AccountItemModel) {
            accountsAdapter.setAccountContentVisibility(item.accountId, !item.isAccountContentVisible)
        }

        override fun copyAccountItemLogin(item: AccountItemModel) {
            viewModel.onCopyAccountLoginAction(item.accountId)
        }

        override fun copyAccountItemPassword(item: AccountItemModel) {
            viewModel.onCopyAccountPasswordAction(item.accountId)
        }

        override fun editAccountItem(item: AccountItemModel) {
            viewModel.onEditAccountAction(item.accountId)
        }

        override fun pinAccount(item: AccountItemModel) {
            viewModel.pinAccount(item.accountId)
        }

        override fun unpinAccount(item: AccountItemModel) {
            viewModel.unpinAccount(item.accountId)
        }

        override fun deleteAccountItem(item: AccountItemModel) {
            viewModel.onDeleteAccountAction(item.accountId)
        }

        override fun onShowPopupMenu(item: AccountItemModel) {
            accountsAdapter.setContextMenuOpenedForAccountItem(item.accountId)
        }

        override fun onDismissPopupmenu(item: AccountItemModel) {
            accountsAdapter.clearContextMenuOpenedForAccountItems()
        }
    }

    private var tbAccountsListBarActionMode: ActionMode? = null
    private lateinit var accountsAdapter: AccountsListRecyclerAdapter

    private var deleteAccountDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_accounts_list

    override fun isBackPressEnabled() = false

    override fun injectFragment(component: FeatureTabAccountsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): AccountsListViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountsActionModeViewModel = injectViewModel(viewModelsFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPasswords()
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()

        fragmentView.fabAddNewAccountAction.setSafeClickListener {
            navigator.navigateToAddNewAccount()
        }
    }

    override fun subscriberToViewModel(viewModel: AccountsListViewModel) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToPrimaryLoadingState().observe(viewLifecycleOwner, this::setPrimaryLoadingState)
        viewModel.subscribeToSecondaryLoadingState().observe(viewLifecycleOwner, this::setSecondaryLoadingState)
        viewModel.subscribeEmptyAccountsState().observe(viewLifecycleOwner, this::setEmptyAccountsState)
        viewModel.subscribeToAccountsList().observe(viewLifecycleOwner, this::displayAccounts)
        viewModel.subscribeToToolbarScrollingState().observe(viewLifecycleOwner) { isEnabled ->
        }
        viewModel.subscribeToShowEditPasswordScreen().observe(viewLifecycleOwner, this::showEditAccountScreen)

        subscribeToActionModeViewModel(accountsActionModeViewModel)
    }

    override fun onResume() {
        super.onResume()
        deleteAccountDialog?.dismissAllowingStateLoss()
    }

    override fun onPause() {
        super.onPause()
        hideSelectedMode()
    }

    private fun displayAccounts(accountModelsList: List<AccountItemModel>) {
        accountsAdapter.insertData(accountModelsList)
    }

    private fun setPrimaryLoadingState(isLoading: Boolean) {
        fragmentView.accountLoadingContainer.setVisibility(isLoading)
        if (isLoading) {
            fragmentView.loadingAnimation.playAnimation()
        } else {
            fragmentView.loadingAnimation.pauseAnimation()
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

    private fun setEmptyAccountsState(isEmptyViewVisible: Boolean) {
        fragmentView.incAccountsEmptyView.isVisible = isEmptyViewVisible
    }

    private fun showEditAccountScreen(accountId: Long) {
        navigator.navigateToEditAccount(accountId)
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.accounts_list_toolbar_title)
        }
    }

    private fun initRecyclerView() {
        accountsAdapter = AccountsListRecyclerAdapter(accountsListClickListener)

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

    private fun subscribeToActionModeViewModel(viewModel: AccountsActionModeViewModel) {
        viewModel.subscribeToSelectedModeState().observe(viewLifecycleOwner) { hasSelectedMode ->
            if (hasSelectedMode) displaySelectedMode() else hideSelectedMode()
        }
        viewModel.subscribeToSelectedStateForItem().observe(viewLifecycleOwner) {
            showSelectStateForItem(it.first, it.second)
        }
        viewModel.subscribeToSelectedItemsQuantityText().observe(viewLifecycleOwner, this::showSelectedItemsQuantityText)
    }

    private fun showSelectedItemsQuantityText(text: String) {
        tbAccountsListBarActionMode?.title = text
    }

    private fun displaySelectedMode() {
        val activityOfFragment = requireActivity()
        accountsAdapter.setItemsActionModeState(true)
        if (tbAccountsListBarActionMode == null && activityOfFragment is AppCompatActivity) {
            tbAccountsListBarActionMode =
                activityOfFragment.startSupportActionMode(object : ActionMode.Callback {
                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        return when (item?.itemId) {
                            R.id.action_delete_selected_accounts -> {
                                showDeleteAccountDialog { accountsActionModeViewModel.onDeleteSelectedInActionMode() }
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

    private fun hideSelectedMode() {
        accountsAdapter.setItemsActionModeState(false)
        accountsActionModeViewModel.onSelectedModeFinished()
        tbAccountsListBarActionMode?.finish()
        tbAccountsListBarActionMode = null
    }

    private fun showSelectStateForItem(isSelected: Boolean, accountId: Long) {
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