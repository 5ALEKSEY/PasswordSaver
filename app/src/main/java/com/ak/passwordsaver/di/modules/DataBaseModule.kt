package com.ak.passwordsaver.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.ak.passwordsaver.BuildConfig
import com.ak.passwordsaver.data.model.db.PSDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun providePasswordSaverDatabaseInstance(context: Context): PSDatabase {
        return Room.databaseBuilder(context, PSDatabase::class.java, BuildConfig.DATA_BASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}