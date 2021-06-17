package com.example.zemogatest.data.user.sources

import com.example.zemogatest.data.db.entity.UserEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.flow.Flow

interface UserLocalSource {

    suspend fun updateAll(items: List<UserEntity>): Boolean

    suspend fun createOrUpdate(item: UserEntity): Boolean

    suspend fun createOrUpdate(items: List<UserEntity>): Boolean

    fun getAll(): Flow<List<UserEntity>>

    suspend fun getById(id: String): Optional<UserEntity>

    suspend fun deleteById(id: String): Int

    suspend fun getByName(itemName: String): Int
}