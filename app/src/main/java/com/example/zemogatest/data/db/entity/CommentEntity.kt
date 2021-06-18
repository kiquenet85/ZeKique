package com.example.zemogatest.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"]
        )
    ],
    indices = [Index(value = ["id"], unique = true),
        Index(value = ["postId"], unique = false),
        Index(value = ["userId"], unique = false)
    ],
    primaryKeys = ["id"]
)
data class CommentEntity(
    val id: String,
    val userId: String,
    val postId: String,
    val name: String,
    val body: String
)