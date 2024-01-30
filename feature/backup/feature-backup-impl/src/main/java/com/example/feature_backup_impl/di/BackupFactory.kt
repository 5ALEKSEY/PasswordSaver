package com.example.feature_backup_impl.di

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_appupdate_api.interfaces.IFeaturesUpdateManager
import com.example.feature_backup_impl.backupinfo.BackupInfoViewModel
import com.example.feature_backup_impl.model.interactor.IBackupInteractor
import com.example.feature_backup_impl.model.manager.IBackupImportManager
import com.example.feature_backup_impl.sizebeautifier.ISizeBeautifier
import com.example.feature_backup_impl.timeneautifier.ITimeBeautifier
import javax.inject.Inject

class BackupViewModelsFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>,
) : BaseViewModelFactory(viewModels)

class BackupInfoViewModelAssistedFactory @Inject constructor(
    private val resourceManager: IResourceManager,
    private val interactor: IBackupInteractor,
    private val sizeBeautifier: ISizeBeautifier,
    private val timeBeautifier: ITimeBeautifier,
    private val backupImportManager: IBackupImportManager,
    private val featuresUpdateManager: IFeaturesUpdateManager,
) : IViewModelAssistedFactory<BackupInfoViewModel> {
    override fun create(): BackupInfoViewModel {
        return BackupInfoViewModel(
            interactor = interactor,
            sizeBeautifier = sizeBeautifier,
            timeBeautifier = timeBeautifier,
            backupImportManager = backupImportManager,
            resourceManager= resourceManager,
            featuresUpdateManager = featuresUpdateManager,
        )
    }
}