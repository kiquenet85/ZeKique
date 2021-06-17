package com.example.zemogatest.domain.remote

import com.google.gson.annotations.Expose

class UserResponseDTO(
    @Expose val id: String? = null,
    @Expose val name: String? = null,
    @Expose val username: String? = null,
    @Expose val email: String? = null,
    @Expose val phone: String? = null,
    @Expose val website: String? = null
)