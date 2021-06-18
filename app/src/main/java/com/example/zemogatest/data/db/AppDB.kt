package com.example.zemogatest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zemogatest.data.db.dao.CommentDAO
import com.example.zemogatest.data.db.dao.PostDAO
import com.example.zemogatest.data.db.dao.UserDAO
import com.example.zemogatest.data.db.entity.CommentEntity
import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.data.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        CommentEntity::class
    ], version = 5
)
abstract class AppDB : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun postDAO(): PostDAO
    abstract fun commentDAO(): CommentDAO
}