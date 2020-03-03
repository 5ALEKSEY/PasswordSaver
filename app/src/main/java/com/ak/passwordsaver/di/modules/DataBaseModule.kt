package com.ak.passwordsaver.di.modules

import android.content.Context
import androidx.room.Room
import com.ak.domain.data.model.db.PSDatabase
import com.ak.passwordsaver.BuildConfig
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