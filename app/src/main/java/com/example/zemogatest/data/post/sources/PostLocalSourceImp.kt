package com.example.zemogatest.data.post.sources

import androidx.room.withTransaction
import com.example.zemogatest.data.db.AppDB
import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PostLocalSourceImp @Inject constructor(private val db: AppDB) : PostLocalSource {

    override suspend fun updateAll(items: List<PostEntity>): Boolean {
        val oldItemsId = db.postDAO().getAllId()
        val newItemsId = mutableListOf<String>()

        db.withTransaction {
            items.forEach { toInsert ->
                newItemsId.add(toInsert.id)
            }

            createOrUpdate(items)

            oldItemsId.forEach { oldItemId ->
                if (!newItemsId.contains(oldItemId)) {
                    db.postDAO().delete(oldItemId)
                }
            }
        }
        return true
    }

    override suspend fun createOrUpdate(items: List<PostEntity>): Boolean {
        db.postDAO().insert(items)
        return true
    }

    override suspend fun createOrUpdate(item: PostEntity): Boolean {
        db.postDAO().update(item)
        return true
    }

    override fun getAllAndObserve(): Flow<List<PostEntity>> {
        return db.postDAO().getAllAndObserve()
            .filterNotNull()
            .distinctUntilChanged()
            .conflate()
            .flowOn(Dispatchers.Default)
    }

    override fun getAll(): List<PostEntity> {
        return db.postDAO().getAll()
    }

    override fun getById(id: String): Optional<PostEntity> {
        return db.postDAO().getById(id)?.let {
            Optional.Some(it)
        } ?: Optional.None
    }

    override suspend fun deleteById(id: String): Int {
        return db.postDAO().delete(id)
    }

    override suspend fun getByName(itemTitle: String): Int {
        return db.postDAO().getByTitle(itemTitle)
    }

    override suspend fun deleteAll() : Int {
        return db.postDAO().deleteAll()
    }
}