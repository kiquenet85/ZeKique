package com.example.zemogatest.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.zemogatest.data.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDAO {

    @Query("SELECT * FROM PostEntity where id = :id")
    fun getById(id: String): PostEntity?

    @Query("SELECT * FROM PostEntity")
    fun getAllAndObserve(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity")
    fun getAll(): List<PostEntity>

    @Query("SELECT id FROM PostEntity")
    suspend fun getAllId(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<PostEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entityToUpdate: PostEntity)

    @Query("DELETE FROM PostEntity")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM PostEntity where id = :id")
    suspend fun delete(id: String): Int

    @Query("SELECT count(*) FROM PostEntity where title = :title")
    suspend fun getByTitle(title: String): Int
}