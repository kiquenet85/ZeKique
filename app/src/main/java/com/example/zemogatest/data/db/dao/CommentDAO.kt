package com.example.zemogatest.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zemogatest.data.db.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDAO {

    @Query("SELECT * FROM CommentEntity where id = :id")
    fun getById(id: String): CommentEntity?

    @Query("SELECT * FROM CommentEntity")
    fun getAll(): Flow<List<CommentEntity>>

    @Query("SELECT id FROM CommentEntity")
    suspend fun getAllId(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityToInsert: CommentEntity)

    @Query("DELETE FROM CommentEntity where id IN(:listIds)")
    suspend fun deleteAll(listIds: List<String>): Int

    @Query("DELETE FROM CommentEntity where id = :id")
    suspend fun delete(id: String): Int
}