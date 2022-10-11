package com.ak.feature_tabpasswords_impl.screens.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ak.base.ui.BasePSFragment
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponentInitializer
import com.ak.feature_tabpasswords_impl.di.modules.TabPasswordsViewModelsModule
import com.ak.feature_tabpasswords_impl.screens.navigation.cross.PasswordsTabCrossModuleNavigatorProvider
import com.ak.feature_tabpasswords_impl.screens.navigation.inside.IPasswordsTabNavigator
import javax.inject.Inject
import javax.inject.Named

abstract class BasePasswordsModuleFragment<VM : BasePSViewModel> : BasePSFragment<VM>() {

    @Inject
    internal lateinit var navigator: IPasswordsTabNavigator

    @Inject
    @field:Named(TabPasswordsViewModelsModule.PASSWORDS_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

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

    override fun injectFragment(appContext: Context) {
        if (appContext is FeatureTabPasswordsComponentInitializer) {
            injectFragment(appContext.initializeTabPasswordsComponent())
        }
    }

    abstract fun injectFragment(component: FeatureTabPasswordsComponent)
}