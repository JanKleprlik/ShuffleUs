package com.shuffleus.app.di


import android.content.Context
import com.shuffleus.app.repository.room.AppDatabase
import com.shuffleus.app.repository.room.GroupNamesDao
import com.shuffleus.app.repository.room.UserDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideGroupNamesDao(appDatabase: AppDatabase): GroupNamesDao {
        return appDatabase.groupNamesDao()
    }
}