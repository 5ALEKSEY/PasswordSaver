package com.ak.feature_tabaccounts_impl.screens.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ak.base.ui.BasePSFragment
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponent
import com.ak.feature_tabaccounts_impl.di.FeatureTabAccountsComponentInitializer
import com.ak.feature_tabaccounts_impl.di.modules.TabAccountsViewModelsModule
import com.ak.feature_tabaccounts_impl.screens.navigation.inside.IAccountsTabNavigator
import javax.inject.Inject
import javax.inject.Named

abstract class BaseAccountsModuleFragment<VM : BasePSViewModel> : BasePSFragment<VM>() {

    @Inject
    internal lateinit var navigator: IAccountsTabNavigator

    @Inject
    @field:Named(TabAccountsViewModelsModule.ACCOUNTS_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.applicationContext?.let {
            navigator.setupNavigator(navController)
        }
    }

    override fun injectFragment(appContext: Context) {
        if (appContext is FeatureTabAccountsComponentInitializer) {
            injectFragment(appContext.initializeTabAccountsComponent())
        }
    }

    abstract fun injectFragment(component: FeatureTabAccountsComponent)
}