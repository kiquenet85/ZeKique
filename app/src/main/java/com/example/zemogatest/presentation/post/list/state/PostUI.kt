package com.example.zemogatest.presentation.post.list.state

import com.example.zemogatest.presentation.base.Identifier

data class PostUI(
    val id: String,
    val title: String,
    val description: String,
    val favorite: Boolean,
    val userId: String,
    val seen: Boolean,
    val showAsNotSeen: Boolean,
    val userUI: UserUI? = null
) : Identifier {
    override fun getIdentifier(): String {
        return id
    }
}

data class UserUI(
    val name: String,
    val email: String,
    val phone: String,
    val website: String
)