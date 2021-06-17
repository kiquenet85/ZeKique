package com.example.zemogatest.domain.remote

import com.google.gson.annotations.Expose

class PostResponseDTO(
    @Expose val id: String? = null,
    @Expose val userId: String? = null,
    @Expose val title: String? = null,
    @Expose val body: String? = null
)