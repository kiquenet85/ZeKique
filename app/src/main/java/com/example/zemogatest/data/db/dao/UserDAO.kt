package com.example.zemogatest.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zemogatest.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Query("SELECT * FROM UserEntity where id = :id")
    suspend fun getById(id: String): UserEntity?

    @Query("SELECT id FROM UserEntity")
    suspend fun getAllId(): List<String>

    @Query("SELECT * FROM UserEntity")
    fun getAll(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityToInsert: UserEntity)

    @Query("DELETE FROM UserEntity")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM UserEntity where id = :id")
    suspend fun delete(id: String): Int

    @Query("SELECT count(*) FROM UserEntity where name = :userName")
    suspend fun getByName(userName: String): Int
}