package com.ak.tabpasswords.presentation.base

import android.os.Bundle
import android.view.View
import com.ak.base.presenter.BasePSPresenter
import com.ak.base.ui.BasePSFragment
import com.ak.tabpasswords.navigation.cross.PasswordsTabCrossModuleNavigatorProvider
import com.ak.tabpasswords.navigation.inside.IPasswordsTabNavigator
import javax.inject.Inject

abstract class BasePasswordsModuleFragment<Presenter : BasePSPresenter<*>> :
    BasePSFragment<Presenter>() {

    @Inject
    internal lateinit var navigator: IPasswordsTabNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.applicationContext?.let {
            val crossNavigator = it as PasswordsTabCrossModuleNavigatorProvider
            navigator.setupNavigator(
                navController,
                crossNavigator.provideCrossNavigatorForPasswordsModule()
            )
        }
    }
}