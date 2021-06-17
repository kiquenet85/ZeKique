package com.example.zemogatest.di

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.data.comment.repository.CommentRepository
import com.example.zemogatest.data.comment.sources.CommentLocalSource
import com.example.zemogatest.data.comment.sources.CommentLocalSourceImp
import com.example.zemogatest.data.comment.sources.CommentRemoteSource
import com.example.zemogatest.data.comment.sources.CommentRemoteSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object CommentServicesModule {
    @Provides
    @ActivityRetainedScoped
    fun provideCommentRepository(
        commentLocalSource: CommentLocalSource,
        commentRemoteSource: CommentRemoteSource,
        errorHandler: ErrorHandler
    ): CommentRepository =
        CommentRepository(
            commentLocalSource,
            commentRemoteSource,
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
}