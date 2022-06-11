package com.ak.feature_tabpasswords_impl.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.add.AddNewPasswordViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraPickImageViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.edit.EditPasswordViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsActionModeViewModel
import com.ak.feature_tabpasswords_impl.screens.presentation.passwords.PasswordsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@Module
abstract class TabPasswordsViewModelsModule {

    companion object {
        const val PASSWORDS_VIEW_MODELS_FACTORY_KEY = "passwords_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(PASSWORDS_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: PasswordsViewModelsFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(PasswordsListViewModel::class)
    abstract fun injectPasswordsListViewModel(viewModel: PasswordsListViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(PasswordsActionModeViewModel::class)
    abstract fun injectPasswordsActionModeViewModel(viewModel: PasswordsActionModeViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(EditPasswordViewModel::class)
    abstract fun injectEditPasswordViewModel(viewModel: EditPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(AddNewPasswordViewModel::class)
    abstract fun injectAddNewPasswordViewModel(viewModel: AddNewPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(CameraPickImageViewModel::class)
    abstract fun injectCameraPickImageViewModel(viewModel: CameraPickImageViewModel): ViewModel
}

class PasswordsViewModelsFactory @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : BaseViewModelFactory(viewModels)