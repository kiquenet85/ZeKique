package com.example.zemogatest.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    foreignKeys = [androidx.room.ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["email"],
        childColumns = ["userId"]
    )],
    indices = [Index(value = ["id"], unique = true),
        Index(value = ["userId"], unique = false)
    ],
    primaryKeys = ["id"]
)
data class PostEntity(
    val id: String,
    val userId: String,
    val title: String,
    val body: String,
    val favorite: Boolean
)