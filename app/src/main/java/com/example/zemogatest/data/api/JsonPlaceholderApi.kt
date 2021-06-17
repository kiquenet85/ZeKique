package com.example.zemogatest.data.api

import com.example.zemogatest.domain.remote.CommentResponseDTO
import com.example.zemogatest.domain.remote.PostResponseDTO
import com.example.zemogatest.domain.remote.UserResponseDTO
import retrofit2.http.GET

interface JsonPlaceholderApi {

    @GET("/post")
    suspend fun getAllPosts(): List<PostResponseDTO>

    @GET("/users")
    suspend fun getAllUsers(): List<UserResponseDTO>

    @GET("/comments")
    suspend fun getAllComments(): List<CommentResponseDTO>
}