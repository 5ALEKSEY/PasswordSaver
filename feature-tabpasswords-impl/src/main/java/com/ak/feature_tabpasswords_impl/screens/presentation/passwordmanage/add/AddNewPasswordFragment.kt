package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add

import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.BaseManagePasswordFragment

class AddNewPasswordFragment : BaseManagePasswordFragment<AddNewPasswordViewModel>() {

    override fun getToolbarTitleText() = getString(R.string.add_new_password_toolbar_title)

    override fun injectFragment(component: FeatureTabPasswordsComponent) {
        component.inject(this)
    }

    override fun createViewModel(): AddNewPasswordViewModel {
        return injectViewModel(viewModelsFactory)
    }
}
