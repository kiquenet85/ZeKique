package com.example.zemogatest.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    indices = [
        Index(value = ["email"], unique = true)
    ],
    primaryKeys = ["email"]
)
data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val website: String
)