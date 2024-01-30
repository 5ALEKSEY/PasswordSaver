package com.example.feature_backup_impl.di.repo

import com.ak.base.scopes.FeatureScope
import com.example.feature_backup_impl.fileshare.IShareFileManager
import com.example.feature_backup_impl.fileshare.ShareFileManagerImpl
import com.example.feature_backup_impl.model.creator.BackupCreatorImpl
import com.example.feature_backup_impl.model.creator.IBackupCreator
import com.example.feature_backup_impl.model.interactor.BackupInteractorImpl
import com.example.feature_backup_impl.model.interactor.IBackupInteractor
import com.example.feature_backup_impl.model.manager.BackupImportManagerImpl
import com.example.feature_backup_impl.model.manager.IBackupImportManager
import com.example.feature_backup_impl.repo.BackupLocalRepoImpl
import com.example.feature_backup_impl.repo.IBackupLocalRepo
import com.example.feature_backup_impl.sizebeautifier.ISizeBeautifier
import com.example.feature_backup_impl.sizebeautifier.SizeBeautifierImpl
import com.example.feature_backup_impl.timeneautifier.ITimeBeautifier
import com.example.feature_backup_impl.timeneautifier.TimeBeautifierImpl
import dagger.Binds
import dagger.Module

@Module
abstract class BackupInfoModule {

    @Binds
    @FeatureScope
    abstract fun bindBackupLocalRepo(
        repoImpl: BackupLocalRepoImpl,
    ): IBackupLocalRepo

    @Binds
    @FeatureScope
    abstract fun bindBackupInteractor(
        interactorImpl: BackupInteractorImpl,
    ): IBackupInteractor

    @Binds
    @FeatureScope
    abstract fun bindSizeBeautifier(
        sizeBeautifierImpl: SizeBeautifierImpl,
    ): ISizeBeautifier

    @Binds
    @FeatureScope
    abstract fun bindShareFileManager(
        shareFileManagerImpl: ShareFileManagerImpl,
    ): IShareFileManager

    @Binds
    @FeatureScope
    abstract fun bindTimeBeautifier(
        timeBeautifierImpl: TimeBeautifierImpl,
    ): ITimeBeautifier

    @Binds
    @FeatureScope
    abstract fun bindCurrentDataBackupCreator(
        backupCreatorImpl: BackupCreatorImpl,
    ): IBackupCreator

    @Binds
    @FeatureScope
    abstract fun bindBackupImportManager(
        backupImportManagerImpl: BackupImportManagerImpl,
    ): IBackupImportManager
}