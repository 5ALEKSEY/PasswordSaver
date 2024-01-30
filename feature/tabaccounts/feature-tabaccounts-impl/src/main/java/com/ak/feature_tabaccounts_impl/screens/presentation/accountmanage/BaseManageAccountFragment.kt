package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage

import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.ak.base.constants.AppConstants
import com.ak.base.extensions.hideKeyboard
import com.ak.base.extensions.setSafeClickListener
import com.ak.base.ui.custom.PsThemedTextInputLayout
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.screens.presentation.base.BaseAccountsModuleFragment
import com.google.android.material.textfield.TextInputEditText

abstract class BaseManageAccountFragment<ManagerVM : BaseManageAccountViewModel>
    : BaseAccountsModuleFragment<ManagerVM>() {

    private var btnManageAccountAction: Button? = null
    private var tietAccountLoginField: TextInputEditText? = null
    private var tietAccountNameField: TextInputEditText? = null
    private var tietAccountPasswordField: TextInputEditText? = null
    private var tilAccountLoginLayout: PsThemedTextInputLayout? = null
    private var tilAccountNameLayout: PsThemedTextInputLayout? = null
    private var tilAccountPasswordLayout: PsThemedTextInputLayout? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_manage_account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun findViews(fragmentView: View) {
        super.findViews(fragmentView)
        with(fragmentView) {
            btnManageAccountAction = findViewById(R.id.btnManageAccountAction)
            tietAccountLoginField = findViewById(R.id.tietAccountLoginField)
            tietAccountNameField = findViewById(R.id.tietAccountNameField)
            tietAccountPasswordField = findViewById(R.id.tietAccountPasswordField)
            tilAccountLoginLayout = findViewById(R.id.tilAccountLoginLayout)
            tilAccountNameLayout = findViewById(R.id.tilAccountNameLayout)
            tilAccountPasswordLayout = findViewById(R.id.tilAccountPasswordLayout)
        }
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()

        tietAccountPasswordField?.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                manageAccountAction()
                true
            } else {
                false
            }
        }

        tietAccountNameField?.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.ACCOUNT_NAME_MAX_LENGTH)
        )
        tietAccountLoginField?.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.ACCOUNT_LOGIN_MAX_LENGTH)
        )
        tietAccountPasswordField?.filters = arrayOf(
            InputFilter.LengthFilter(AppConstants.ACCOUNT_PASSWORD_MAX_LENGTH)
        )

        btnManageAccountAction?.setSafeClickListener {
            manageAccountAction()
        }

        // Theme
        addThemedView(tilAccountNameLayout)
        addThemedView(tilAccountLoginLayout)
        addThemedView(tilAccountPasswordLayout)
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
        tilAccountNameLayout?.error = errorMessage
    }

    private fun displayAccountLoginInputError(errorMessage: String?) {
        tilAccountLoginLayout?.error = errorMessage
    }

    private fun displayAccountPasswordInputError(errorMessage: String?) {
        tilAccountPasswordLayout?.error = errorMessage
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(getToolbarTitleText())
            setupBackAction(R.drawable.ic_back_action) {
                hideKeyboard()
                navController.popBackStack()
            }
        }
    }

    protected abstract fun getToolbarTitleText(): String

    private fun manageAccountAction() {
        hideKeyboard()
        viewModel.onManageAccountAction(
            tietAccountNameField?.text.toString(),
            tietAccountLoginField?.text.toString(),
            tietAccountPasswordField?.text.toString()
        )
    }
}