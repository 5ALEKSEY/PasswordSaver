package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.hideKeyboard
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.screens.presentation.base.BaseAccountsModuleFragment
import kotlinx.android.synthetic.main.fragment_manage_account.view.btnManageAccountAction
import kotlinx.android.synthetic.main.fragment_manage_account.view.tbManageAccountBar
import kotlinx.android.synthetic.main.fragment_manage_account.view.tietAccountLoginField
import kotlinx.android.synthetic.main.fragment_manage_account.view.tietAccountNameField
import kotlinx.android.synthetic.main.fragment_manage_account.view.tietAccountPasswordField
import kotlinx.android.synthetic.main.fragment_manage_account.view.tilAccountLoginLayout
import kotlinx.android.synthetic.main.fragment_manage_account.view.tilAccountNameLayout
import kotlinx.android.synthetic.main.fragment_manage_account.view.tilAccountPasswordLayout

abstract class BaseManageAccountFragment<ManagerVM : BaseManageAccountViewModel>
    : BaseAccountsModuleFragment<ManagerVM>() {

    override fun getFragmentLayoutResId() = R.layout.fragment_manage_account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()

        fragmentView.tietAccountPasswordField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                manageAccountAction()
                true
            } else {
                false
            }
        }

        fragmentView.tietAccountNameField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.ACCOUNT_NAME_MAX_LENGTH)
        )
        fragmentView.tietAccountLoginField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.ACCOUNT_LOGIN_MAX_LENGTH)
        )
        fragmentView.tietAccountPasswordField.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.ACCOUNT_PASSWORD_MAX_LENGTH)
        )

        fragmentView.btnManageAccountAction.setSafeClickListener {
            manageAccountAction()
        }
    }

    override fun subscriberToViewModel(viewModel: ManagerVM) {
        super.subscriberToViewModel(viewModel)
        viewModel.subscribeToSuccessAccountManage().observe(viewLifecycleOwner) {
            navController.popBackStack()
        }
        viewModel.subscribeToNameInputError().observe(viewLifecycleOwner, this::displayAccountNameInputError)
        viewModel.subscribeToLoginInputError().observe(viewLifecycleOwner, this::displayAccountLoginInputError)
        viewModel.subscribeToPasswordInputError().observe(viewLifecycleOwner, this::displayAccountPasswordInputError)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_new_account_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == R.id.action_save_account) {
            manageAccountAction()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun displayAccountNameInputError(errorMessage: String?) {
        fragmentView.tilAccountNameLayout.error = errorMessage
    }

    private fun displayAccountLoginInputError(errorMessage: String?) {
        fragmentView.tilAccountLoginLayout.error = errorMessage
    }

    private fun displayAccountPasswordInputError(errorMessage: String?) {
        fragmentView.tilAccountPasswordLayout.error = errorMessage
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                val actionBarView = fragmentView.tbManageAccountBar
                setSupportActionBar(actionBarView)
                supportActionBar?.title = getToolbarTitleText()
                actionBarView.setNavigationOnClickListener {
                    hideKeyboard()
                    navController.popBackStack()
                }
            }
        }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun manageAccountAction() {
        hideKeyboard()
        viewModel.onManageAccountAction(
            fragmentView.tietAccountNameField.text.toString(),
            fragmentView.tietAccountLoginField.text.toString(),
            fragmentView.tietAccountPasswordField.text.toString()
        )
    }
}