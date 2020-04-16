package com.ak.core_repo_impl.di.module

import android.content.Context
import androidx.room.Room
import com.ak.core_repo_impl.BuildConfig
import com.ak.core_repo_impl.data.model.db.PSDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providePasswordSaverDatabaseInstance(context: Context): PSDatabase {
        return Room.databaseBuilder(context, PSDatabase::class.java, BuildConfig.DATA_BASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}