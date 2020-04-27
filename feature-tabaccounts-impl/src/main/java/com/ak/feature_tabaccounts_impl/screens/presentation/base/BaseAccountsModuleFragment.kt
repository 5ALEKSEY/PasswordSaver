package com.ak.feature_tabaccounts_impl.screens.presentation.base

import android.os.Bundle
import android.view.View
import com.ak.base.presenter.BasePSPresenter
import com.ak.base.ui.BasePSFragment
import com.ak.feature_tabaccounts_impl.screens.navigation.inside.IAccountsTabNavigator
import javax.inject.Inject

abstract class BaseAccountsModuleFragment<Presenter : BasePSPresenter<*>> :
    BasePSFragment<Presenter>() {

    @Inject
    internal lateinit var navigator: IAccountsTabNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.applicationContext?.let {
            navigator.setupNavigator(navController)
        }
    }
}