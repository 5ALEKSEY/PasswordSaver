package com.ak.feature_security_impl.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.feature_security_impl.auth.SecurityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class SecurityViewModelsModule {

    companion object {
        const val SECURITY_VIEW_MODELS_FACTORY_KEY = "security_view_models_factory_key"
    }

    @Binds
    @Singleton
    @Named(SECURITY_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: SecurityViewModelsFactoryComposite): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton
    @ViewModelInjectKey(SecurityViewModel::class)
    abstract fun bindSecurityViewModelAssistedFactory(
        factory: SecurityViewModelAssistedFactory
    ): IViewModelAssistedFactory<out ViewModel>
}