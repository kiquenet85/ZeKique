package com.example.zemogatest.di

import android.content.Context
import androidx.room.Room
import com.example.zemogatest.data.db.AppDB
import com.example.zemogatest.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesDB(@ApplicationContext appContext: Context): AppDB {
        return Room.databaseBuilder(appContext, AppDB::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    }
    @Provides
    @Singleton
    fun providesDBDispatcher(): CoroutineDispatcher {
        return Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }
}