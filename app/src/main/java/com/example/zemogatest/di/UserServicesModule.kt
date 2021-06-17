package com.example.zemogatest.di

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.data.user.repository.UserRepository
import com.example.zemogatest.data.user.sources.UserLocalSource
import com.example.zemogatest.data.user.sources.UserLocalSourceImp
import com.example.zemogatest.data.user.sources.UserRemoteSource
import com.example.zemogatest.data.user.sources.UserRemoteSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object UserServicesModule {
    @Provides
    @ActivityRetainedScoped
    fun provideUserRepository(
        userLocalSource: UserLocalSource,
        userRemoteSource: UserRemoteSource,
        errorHandler: ErrorHandler
    ): UserRepository =
        UserRepository(
            userLocalSource,
            userRemoteSource,
            errorHandler
        )

    @Provides
    @ActivityRetainedScoped
    fun provideUserLocalSource(userLocalSourceImp: UserLocalSourceImp): UserLocalSource =
        userLocalSourceImp

    @Provides
    @ActivityRetainedScoped
    fun provideUserRemoteSource(userRemoteSourceImp: UserRemoteSourceImp): UserRemoteSource =
        userRemoteSourceImp
}