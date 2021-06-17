package com.example.zemogatest.data.post.sources

import com.example.zemogatest.domain.remote.PostResponseDTO

interface PostRemoteSource {
    suspend fun getAll(): List<PostResponseDTO>
}