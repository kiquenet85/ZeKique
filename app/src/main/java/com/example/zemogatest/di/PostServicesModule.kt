package com.example.zemogatest.di

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.data.api.JsonPlaceholderApi
import com.example.zemogatest.data.comment.sources.CommentLocalSource
import com.example.zemogatest.data.comment.sources.CommentLocalSourceImp
import com.example.zemogatest.data.comment.sources.CommentRemoteSource
import com.example.zemogatest.data.comment.sources.CommentRemoteSourceImp
import com.example.zemogatest.data.post.sources.PostLocalSource
import com.example.zemogatest.data.post.sources.PostLocalSourceImp
import com.example.zemogatest.data.post.sources.PostRemoteSource
import com.example.zemogatest.data.post.sources.PostRemoteSourceImp
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
object PostServicesModule {
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
    fun provideCommentLocalSource(commentLocalSourceImp: CommentLocalSourceImp): CommentLocalSource =
        commentLocalSourceImp

    @Provides
    @ActivityRetainedScoped
    fun provideCommentRemoteSource(commentRemoteSourceImp: CommentRemoteSourceImp): CommentRemoteSource =
        commentRemoteSourceImp

    @Provides
    @ActivityRetainedScoped
    fun provideUserAPI(networkManager: NetworkManager): JsonPlaceholderApi =
        networkManager.defaultRetrofit.create(JsonPlaceholderApi::class.java)
}