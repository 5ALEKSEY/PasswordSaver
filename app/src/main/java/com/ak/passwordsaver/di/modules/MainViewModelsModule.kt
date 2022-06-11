package com.ak.passwordsaver.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.passwordsaver.presentation.screens.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
abstract class MainViewModelsModule {

    companion object {
        const val MAIN_VIEW_MODELS_FACTORY_KEY = "main_view_models_factory_key"
    }

    @Binds
    @Singleton
    @Named(MAIN_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: MainViewModelsFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelInjectKey(HomeViewModel::class)
    abstract fun injectHomeVM(viewModel: HomeViewModel): ViewModel
}

class MainViewModelsFactory @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : BaseViewModelFactory(viewModels)