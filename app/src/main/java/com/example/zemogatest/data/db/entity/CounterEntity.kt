package com.example.zemogatest.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    indices = [Index(value = ["id"], unique = true)],
    primaryKeys = ["id"]
)
data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val phone: Int,
    val website: String
)