package com.example.zemogatest.data.comment.sources

import com.example.zemogatest.domain.remote.CommentResponseDTO

interface CommentRemoteSource {
    suspend fun getAll(): List<CommentResponseDTO>
}