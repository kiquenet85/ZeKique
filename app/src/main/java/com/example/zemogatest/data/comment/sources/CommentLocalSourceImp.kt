package com.example.zemogatest.data.comment.sources

import androidx.room.withTransaction
import com.example.zemogatest.data.db.AppDB
import com.example.zemogatest.data.db.entity.CommentEntity
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentLocalSourceImp @Inject constructor(private val db: AppDB) : CommentLocalSource {

    override suspend fun updateAll(items: List<CommentEntity>): Boolean {
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
        return true
    }

    override suspend fun createOrUpdate(items: List<CommentEntity>): Boolean {
        db.commentDAO().insert(items)
        return true
    }

    override suspend fun createOrUpdate(item: CommentEntity): Boolean {
        db.commentDAO().insert(item)
        return true
    }

    override fun getAll(): Flow<List<CommentEntity>> {
        return db.commentDAO().getAll()
            .filterNotNull()
            .distinctUntilChanged()
            .conflate()
            .flowOn(Dispatchers.Default)
    }

    override fun getById(id: String): Optional<CommentEntity> {
        return db.commentDAO().getById(id)?.let {
            Optional.Some(it)
        } ?: Optional.None
    }

    override suspend fun deleteById(id: String): Int {
        return db.commentDAO().delete(id)
    }
}