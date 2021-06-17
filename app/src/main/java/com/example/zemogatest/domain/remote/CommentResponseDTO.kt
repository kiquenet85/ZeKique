package com.example.zemogatest.domain.remote

import com.google.gson.annotations.Expose

class CommentResponseDTO(
    @Expose val id: String? = null,
    @Expose val postId: String? = null,
    @Expose val name: String? = null,
    @Expose val email: String? = null,
    @Expose val body: String? = null
)