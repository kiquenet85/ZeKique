package com.example.zemogatest.data.comment.sources

import com.example.zemogatest.data.api.JsonPlaceholderApi
import com.example.zemogatest.domain.remote.CommentResponseDTO
import javax.inject.Inject

class CommentRemoteSourceImp @Inject constructor(val api: JsonPlaceholderApi) :
    CommentRemoteSource {

    override suspend fun getAll(): List<CommentResponseDTO> {
        return api.getAllComments()
    }
}