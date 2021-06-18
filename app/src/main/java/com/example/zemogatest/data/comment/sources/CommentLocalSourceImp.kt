package com.example.zemogatest.data.comment.sources

import androidx.room.withTransaction
import com.example.zemogatest.data.db.AppDB
import com.example.zemogatest.data.db.entity.CommentEntity
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

class CommentLocalSourceImp @Inject constructor(
    private val db: AppDB,
    private val dbDispatcher: CoroutineDispatcher
) : CommentLocalSource {

    override suspend fun updateAll(items: List<CommentEntity>): Boolean =
        withContext(dbDispatcher) {
            val oldItemsId = db.commentDAO().getAllId()
            val newItemsId = mutableListOf<String>()

            db.withTransaction {
                items.forEach { toInsert ->
                    newItemsId.add(toInsert.id)
                }

                createOrUpdate(items)

                oldItemsId.forEach { oldItemId ->
                    if (!newItemsId.contains(oldItemId)) {
                        db.commentDAO().delete(oldItemId)
                    }
                }
            }
            true
        }

    override suspend fun createOrUpdate(items: List<CommentEntity>): Boolean =
        withContext(dbDispatcher) {
            db.commentDAO().insert(items)
            true
        }

    override suspend fun createOrUpdate(item: CommentEntity): Boolean = withContext(dbDispatcher) {
        db.commentDAO().insert(item)
        true
    }

    override fun getAll(): Flow<List<CommentEntity>> {
        return db.commentDAO().getAll()
            .filterNotNull()
            .distinctUntilChanged()
            .conflate()
            .flowOn(Dispatchers.Default)
    }

    override suspend fun getById(id: String): Optional<CommentEntity> = withContext(dbDispatcher) {
        db.commentDAO().getById(id)?.let {
            Optional.Some(it)
        } ?: Optional.None
    }

    override suspend fun deleteById(id: String): Int = withContext(dbDispatcher) {
        db.commentDAO().delete(id)
    }
}