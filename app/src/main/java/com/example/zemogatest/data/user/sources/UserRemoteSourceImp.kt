package com.example.zemogatest.data.user.sources

import com.example.zemogatest.data.api.JsonPlaceholderApi
import com.example.zemogatest.domain.remote.UserResponseDTO
import javax.inject.Inject

class UserRemoteSourceImp @Inject constructor(val api: JsonPlaceholderApi) : UserRemoteSource {

    override suspend fun getAll(): List<UserResponseDTO> {
        return api.getAllUsers()
    }
}