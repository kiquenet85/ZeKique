package com.example.zemogatest.di

import android.content.Context
import com.example.zemogatest.R
import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.common.network.WifiReceiver
import com.example.zemogatest.common.settings.Settings
import com.example.zemogatest.util.FileUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun providesErrorHandler() = ErrorHandler()

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext application: Context, settings: Settings): NetworkManager {
        return NetworkManager(application, settings)
    }

    @Provides
    @Singleton
    fun provideWifiReceiver(networkManager: NetworkManager): WifiReceiver {
        return WifiReceiver(networkManager)
    }

    @Provides
    @Singleton
    fun provideSettings(
        @ApplicationContext appContext: Context,
        @Named("CornershopGson") gson: Gson
    ): Settings {
        return gson.fromJson(
            FileUtil.readFile(appContext, R.raw.default_config), Settings::class.java
        )
    }

    @Provides
    @Singleton
    @Named("CornershopGson")
    fun provideGsonObject(): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }
}