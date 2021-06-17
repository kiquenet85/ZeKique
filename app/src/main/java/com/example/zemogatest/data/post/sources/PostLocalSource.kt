package com.example.zemogatest.data.post.sources

import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.flow.Flow

interface PostLocalSource {

    suspend fun updateAll(items: List<PostEntity>): Boolean

    suspend fun createOrUpdate(item: PostEntity): Boolean

    suspend fun createOrUpdate(items: List<PostEntity>): Boolean

    fun getAllAndObserve(): Flow<List<PostEntity>>

    fun getAll(): List<PostEntity>

    fun getById(id: String): Optional<PostEntity>

    suspend fun deleteById(id: String): Int

    suspend fun getByName(itemTitle: String): Int
}