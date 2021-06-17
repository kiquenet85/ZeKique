package com.example.zemogatest.data.user.sources

import com.example.zemogatest.domain.remote.UserResponseDTO

interface UserRemoteSource {
    suspend fun getAll(): List<UserResponseDTO>
}