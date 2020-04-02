package com.ak.tabpasswords.presentation.passwordmanage.add

import com.ak.tabpasswords.presentation.passwordmanage.BaseManagePasswordFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class AddNewPasswordFragment : BaseManagePasswordFragment<AddNewPasswordPresenter>(),
    IAddNewPasswordView {

    @InjectPresenter
    lateinit var addNewPasswordPresenter: AddNewPasswordPresenter

    @ProvidePresenter
    fun providePresenter(): AddNewPasswordPresenter = daggerPresenter

    override fun getPresenter() = addNewPasswordPresenter

    override fun getToolbarTitleText() = "Add new password"
}
