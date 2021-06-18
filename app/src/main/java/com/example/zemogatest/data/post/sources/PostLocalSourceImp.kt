package com.example.zemogatest.data.post.sources

import androidx.room.withTransaction
import com.example.zemogatest.data.db.AppDB
import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostLocalSourceImp @Inject constructor(
    private val db: AppDB,
    private val dbDispatcher: CoroutineDispatcher
) : PostLocalSource {

    override suspend fun updateAll(items: List<PostEntity>): Boolean = withContext(dbDispatcher) {
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
        true
    }

    override suspend fun createOrUpdate(items: List<PostEntity>): Boolean =
        withContext(dbDispatcher) {
            db.postDAO().insert(items)
            true
        }

    override suspend fun createOrUpdate(item: PostEntity): Boolean = withContext(dbDispatcher) {
        db.postDAO().update(item)
        true
    }

    override fun getAllAndObserve(): Flow<List<PostEntity>> {
        return db.postDAO().getAllAndObserve()
            .filterNotNull()
            .distinctUntilChanged()
            .conflate()
            .flowOn(Dispatchers.Default)
    }

    override suspend fun getAll(): List<PostEntity> = withContext(dbDispatcher) {
        db.postDAO().getAll()
    }

    override suspend fun getById(id: String): Optional<PostEntity> = withContext(dbDispatcher) {
        db.postDAO().getById(id)?.let {
            Optional.Some(it)
        } ?: Optional.None
    }

    override suspend fun deleteById(id: String): Int = withContext(dbDispatcher) {
        db.postDAO().delete(id)
    }

    override suspend fun getByName(itemTitle: String): Int = withContext(dbDispatcher) {
        db.postDAO().getByTitle(itemTitle)
    }

    override suspend fun deleteAll(): Int = withContext(dbDispatcher) {
        db.postDAO().deleteAll()
    }

    override suspend fun delete(id: String): Int = withContext(dbDispatcher) {
        db.postDAO().delete(id)
    }
}