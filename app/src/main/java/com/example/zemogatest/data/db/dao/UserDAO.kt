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
    fun getById(id: String): Flow<UserEntity?>

    @Query("SELECT id FROM UserEntity")
    suspend fun getAllId(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityToInsert: UserEntity)

    @Query("DELETE FROM UserEntity where id IN(:listIds)")
    suspend fun deleteAll(listIds: List<String>): Int

    @Query("DELETE FROM UserEntity where id = :id")
    suspend fun delete(id: String): Int

    @Query("SELECT count(*) FROM UserEntity where name = :userName")
    suspend fun getByName(userName: String): Int
}