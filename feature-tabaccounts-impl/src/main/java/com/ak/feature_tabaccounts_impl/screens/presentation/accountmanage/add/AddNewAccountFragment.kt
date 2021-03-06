package com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.add

import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.screens.presentation.accountmanage.BaseManageAccountFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class AddNewAccountFragment : BaseManageAccountFragment<AddNewAccountPresenter>(),
    IAddNewAccountView {

    @InjectPresenter
    lateinit var addNewPasswordPresenter: AddNewAccountPresenter

    @ProvidePresenter
    fun providePresenter(): AddNewAccountPresenter = daggerPresenter

    override fun getPresenter() = addNewPasswordPresenter

    override fun getToolbarTitleText() = getString(R.string.add_new_account_toolbar_title)

    override fun injectFragment() {
        FeatureTabAccountsComponent.get().inject(this)
    }
}
