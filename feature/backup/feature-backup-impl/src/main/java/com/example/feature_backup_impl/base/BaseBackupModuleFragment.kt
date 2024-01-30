package com.example.feature_backup_impl.base

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.ak.base.ui.BasePSFragment
import com.ak.base.viewmodel.BasePSViewModel
import com.example.feature_backup_impl.di.BackupViewModelsModule
import com.example.feature_backup_impl.di.FeatureBackupComponent
import com.example.feature_backup_impl.di.FeatureBackupComponentInitializer
import javax.inject.Inject
import javax.inject.Named

abstract class BaseBackupModuleFragment<VM : BasePSViewModel> : BasePSFragment<VM>() {

    @Inject
    @field:Named(BackupViewModelsModule.BACKUP_VIEW_MODELS_FACTORY_KEY)
    protected lateinit var viewModelsFactory: ViewModelProvider.Factory

    override fun injectFragment(appContext: Context) {
        if (appContext is FeatureBackupComponentInitializer) {
            injectFragment(appContext.initializeFeatureBackupComponent())
        }
    }

    abstract fun injectFragment(component: FeatureBackupComponent)
}