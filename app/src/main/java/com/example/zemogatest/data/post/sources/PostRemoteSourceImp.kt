package com.example.zemogatest.data.post.sources

import com.example.zemogatest.data.api.JsonPlaceholderApi
import com.example.zemogatest.data.user.sources.UserRemoteSource
import com.example.zemogatest.domain.remote.PostResponseDTO
import javax.inject.Inject

class PostRemoteSourceImp @Inject constructor(val api: JsonPlaceholderApi) : PostRemoteSource {

    override suspend fun getAll(): List<PostResponseDTO> {
        return api.getAllPosts()
    }
}