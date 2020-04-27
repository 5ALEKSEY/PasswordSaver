package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.edit

import android.os.Bundle
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountFragment
import kotlinx.android.synthetic.main.fragment_manage_account.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class EditAccountFragment : BaseManageAccountFragment<EditAccountPresenter>(),
    IEditAccountView {

    companion object {
        const val ACCOUNT_ID_FOR_EDIT_EXTRA_KEY = "account_id_for_edit"
    }

    @InjectPresenter
    lateinit var editAcountPresenter: EditAccountPresenter

    @ProvidePresenter
    fun providePresenter(): EditAccountPresenter = daggerPresenter

    override fun getPresenter() = editAcountPresenter

    override fun getToolbarTitleText() = "Edit account"

    override fun injectFragment() {
        FeatureTabAccountsComponent.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.containsKey(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY) == true) {
            editAcountPresenter.loadPasswordData(
                arguments!!.getLong(ACCOUNT_ID_FOR_EDIT_EXTRA_KEY, 0L)
            )
        } else {
            throw IllegalStateException("Can't open EditPasswordFragment without arguments")
        }
    }

    override fun displayAccountData(accountName: String, accountLogin: String, accountPassword: String) {
        tietAccountNameField.setText(accountName)
        tietAccountLoginField.setText(accountLogin)
        tietAccountPasswordField.setText(accountPassword)
    }
}
