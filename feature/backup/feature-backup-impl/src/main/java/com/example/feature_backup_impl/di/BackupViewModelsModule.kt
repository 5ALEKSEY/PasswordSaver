package com.example.feature_backup_impl.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.base.scopes.FeatureScope
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.base.viewmodel.annotation.ViewModelInjectKey
import com.example.feature_backup_impl.backupinfo.BackupInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class BackupViewModelsModule {

    companion object {
        const val BACKUP_VIEW_MODELS_FACTORY_KEY = "backup_view_models_factory_key"
    }

    @Binds
    @FeatureScope
    @Named(BACKUP_VIEW_MODELS_FACTORY_KEY)
    abstract fun bindViewModelFactory(factory: BackupViewModelsFactoryComposite): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FeatureScope
    @ViewModelInjectKey(BackupInfoViewModel::class)
    abstract fun bindSettingsViewModelAssistedFactory(
        factory: BackupInfoViewModelAssistedFactory,
    ): IViewModelAssistedFactory<out ViewModel>
}