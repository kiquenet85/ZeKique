package com.example.zemogatest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zemogatest.data.db.dao.UserDAO
import com.example.zemogatest.data.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class
    ], version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract fun userDAO() : UserDAO
}