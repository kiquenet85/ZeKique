package com.example.zemogatest.di

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.data.api.JsonPlaceholderApi
import com.example.zemogatest.data.post.repository.PostRepository
import com.example.zemogatest.data.post.sources.PostLocalSource
import com.example.zemogatest.data.post.sources.PostLocalSourceImp
import com.example.zemogatest.data.post.sources.PostRemoteSource
import com.example.zemogatest.data.post.sources.PostRemoteSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object PostServicesModule {
    @Provides
    @ActivityRetainedScoped
    fun providePostRepository(
        postLocalSource: PostLocalSource,
        postRemoteSource: PostRemoteSource,
        errorHandler: ErrorHandler
    ): PostRepository =
        PostRepository(
            postLocalSource,
            postRemoteSource,
            errorHandler
        )

    @Provides
    @ActivityRetainedScoped
    fun providePostLocalSource(postLocalSourceImp: PostLocalSourceImp): PostLocalSource =
        postLocalSourceImp

    @Provides
    @ActivityRetainedScoped
    fun providePostRemoteSource(postRemoteSourceImp: PostRemoteSourceImp): PostRemoteSource =
        postRemoteSourceImp

    @Provides
    @ActivityRetainedScoped
    fun provideUserAPI(networkManager: NetworkManager): JsonPlaceholderApi =
        networkManager.defaultRetrofit.create(JsonPlaceholderApi::class.java)
}