package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.hideKeyboard
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.screens.presentation.base.BaseAccountsModuleFragment
import kotlinx.android.synthetic.main.fragment_manage_account.*

abstract class BaseManageAccountFragment<ManagePresenter : BaseManageAccountPresenter<*>>
    : BaseAccountsModuleFragment<ManagePresenter>(), IBaseManageAccountView {

    protected abstract fun getPresenter(): ManagePresenter

    override fun getFragmentLayoutResId() = R.layout.fragment_manage_account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initViewBeforePresenterAttach() {
        super.initViewBeforePresenterAttach()
        initToolbar()

        tietAccountPasswordField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                manageAccountAction()
                true
            } else {
                false
            }
        }


        tietAccountNameField.filters = arrayOf(
                InputFilter.LengthFilter(AppConstants.ACCOUNT_NAME_MAX_LENGTH)
        )
        tietAccountLoginField.filters = arrayOf(
                InputFilter.LengthFilter(AppConstants.ACCOUNT_LOGIN_MAX_LENGTH)
        )
        tietAccountPasswordField.filters = arrayOf(
                InputFilter.LengthFilter(AppConstants.ACCOUNT_PASSWORD_MAX_LENGTH)
        )

        btnManageAccountAction.setSafeClickListener {
            manageAccountAction()
        }
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

    override fun displaySuccessAccountManageAction() {
        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    override fun displayAccountNameInputError(errorMessage: String) {
        tilAccountNameLayout.error = errorMessage
    }

    override fun hideAccountNameInputError() {
        tilAccountNameLayout.error = null
    }

    override fun displayAccountLoginInputError(errorMessage: String) {
        tilAccountLoginLayout.error = errorMessage
    }

    override fun hideAccountLoginInputError() {
        tilAccountLoginLayout.error = null
    }

    override fun displayAccountPasswordInputError(errorMessage: String) {
        tilAccountPasswordLayout.error = errorMessage
    }

    override fun hideAccountPasswordInputError() {
        tilAccountPasswordLayout.error = null
    }

    private fun initToolbar() {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(tbManageAccountBar)
                supportActionBar?.title = getToolbarTitleText()
                tbManageAccountBar.setNavigationOnClickListener {
                    hideKeyboard()
                    navController.popBackStack()
                }
            }
        }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun manageAccountAction() {
        hideKeyboard()
        hideAccountNameInputError()
        hideAccountLoginInputError()
        hideAccountPasswordInputError()
        getPresenter().onManageAccountAction(
                tietAccountNameField.text.toString(),
                tietAccountLoginField.text.toString(),
                tietAccountPasswordField.text.toString()
        )
    }
}