package com.ak.passwordsaver.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.passwordsaver.presentation.screens.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class MainViewModelsModule {

    companion object {
        const val MAIN_VIEW_MODELS_FACTORY_KEY = "main_view_models_factory_key"
    }

    @Binds
    @Singleton
    @Named(MAIN_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: MainViewModelsFactoryComposite): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelInjectKey(HomeViewModel::class)
    abstract fun bindHomeViewModelAssistedFactory(
        factory: HomeViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>
}