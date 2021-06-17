package com.example.zemogatest.data.user.sources

import androidx.room.withTransaction
import com.example.zemogatest.data.db.AppDB
import com.example.zemogatest.data.db.entity.UserEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalSourceImp @Inject constructor(private val db: AppDB) : UserLocalSource {

    override suspend fun updateAll(items: List<UserEntity>): Boolean {
        val oldItemsId = db.userDAO().getAllId()
        val newItemsId = mutableListOf<String>()

        db.withTransaction {
            items.forEach { toInsert ->
                newItemsId.add(toInsert.id)
            }

            createOrUpdate(items)

            oldItemsId.forEach { oldItemId ->
                if (!newItemsId.contains(oldItemId)) {
                    db.userDAO().delete(oldItemId)
                }
            }
        }
        return true
    }

    override suspend fun createOrUpdate(items: List<UserEntity>): Boolean {
        db.userDAO().insert(items)
        return true
    }

    override suspend fun createOrUpdate(item: UserEntity): Boolean {
        db.userDAO().insert(item)
        return true
    }

    override fun getAll(): Flow<List<UserEntity>> {
        return db.userDAO().getAll()
            .filterNotNull()
            .distinctUntilChanged()
            .conflate()
            .flowOn(Dispatchers.Default)
    }

    override suspend fun getById(id: String): Optional<UserEntity> {
        return db.userDAO().getById(id)?.let {
            Optional.Some(it)
        } ?: Optional.None
    }

    override suspend fun deleteById(id: String): Int {
        return db.userDAO().delete(id)
    }

    override suspend fun getByName(itemName: String): Int {
        return db.userDAO().getByName(itemName)
    }
}