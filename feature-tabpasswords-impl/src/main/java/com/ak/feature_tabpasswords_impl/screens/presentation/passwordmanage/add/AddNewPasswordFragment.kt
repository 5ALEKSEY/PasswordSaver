package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add

import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class AddNewPasswordFragment : BaseManagePasswordFragment<AddNewPasswordPresenter>(),
    IAddNewPasswordView {

    @InjectPresenter
    lateinit var addNewPasswordPresenter: AddNewPasswordPresenter

    @ProvidePresenter
    fun providePresenter(): AddNewPasswordPresenter = daggerPresenter

    override fun getPresenter() = addNewPasswordPresenter

    override fun getToolbarTitleText() = getString(R.string.add_new_password_toolbar_title)

    override fun injectFragment() {
        FeatureTabPasswordsComponent.get().inject(this)
    }
}
