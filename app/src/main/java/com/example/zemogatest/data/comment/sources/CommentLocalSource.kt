package com.example.zemogatest.data.comment.sources

import com.example.zemogatest.data.db.entity.CommentEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.flow.Flow

interface CommentLocalSource {

    suspend fun updateAll(items: List<CommentEntity>): Boolean

    suspend fun createOrUpdate(item: CommentEntity): Boolean

    suspend fun createOrUpdate(items: List<CommentEntity>): Boolean

    fun getAll(): Flow<List<CommentEntity>>

    suspend fun getById(id: String): Optional<CommentEntity>

    suspend fun deleteById(id: String): Int
}