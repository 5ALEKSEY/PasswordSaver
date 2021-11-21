package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import android.os.Bundle
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountFragment
import kotlinx.android.synthetic.main.fragment_manage_account.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class EditAccountFragment : BaseManageAccountFragment<EditAccountPresenter>(),
    IEditAccountView {

    companion object {
        const val ACCOUNT_ID_FOR_EDIT_EXTRA_KEY = "account_id_for_edit"
    }

    @InjectPresenter
    lateinit var editAccountPresenter: EditAccountPresenter

    @ProvidePresenter
    fun providePresenter(): EditAccountPresenter = daggerPresenter

    override fun getPresenter() = editAccountPresenter

    override fun getToolbarTitleText() = getString(R.string.add_edit_account_toolbar_title)

    override fun injectFragment() {
        FeatureTabAccountsComponent.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.containsKey(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY) == true) {
            editAccountPresenter.loadPasswordData(
                arguments!!.getLong(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY, 0L)
            )
        } else {
            throw IllegalStateException("Can't open EditPasswordFragment without arguments")
        }
    }

    override fun displayAccountData(accountName: String, accountLogin: String, accountPassword: String) {
        fragmentView.tietAccountNameField.setText(accountName)
        fragmentView.tietAccountLoginField.setText(accountLogin)
        fragmentView.tietAccountPasswordField.setText(accountPassword)
    }
}
