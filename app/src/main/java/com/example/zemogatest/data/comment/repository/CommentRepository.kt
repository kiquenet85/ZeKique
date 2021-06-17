package com.example.zemogatest.data.comment.repository

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.repository.base.operation.RepositoryReadOperation
import com.example.zemogatest.data.comment.sources.CommentLocalSource
import com.example.zemogatest.data.comment.sources.CommentRemoteSource
import com.example.zemogatest.data.db.entity.CommentEntity
import com.example.zemogatest.domain.remote.CommentResponseDTO
import com.example.zemogatest.util.EMPTY_STRING
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val localSource: CommentLocalSource,
    private val remoteSource: CommentRemoteSource,
    private val errorHandler: ErrorHandler
) {

    suspend fun getAll(infoItem: CommentInfo): Flow<List<CommentEntity>> {
        val operation = object :
            RepositoryReadOperation<List<CommentResponseDTO>, List<CommentEntity>, CommentInfo, List<CommentEntity>> {

            override suspend fun shouldGoRemote(info: CommentInfo): Boolean {
                return infoItem.requiresRemote
            }

            override suspend fun endpoint(info: CommentInfo): List<CommentResponseDTO> {
                return remoteSource.getAll()
            }

            override suspend fun transformRemoteResult(
                remoteData: List<CommentResponseDTO>,
                info: CommentInfo
            ): List<CommentEntity> {
                return remoteData.map {
                    CommentEntity(
                        it.id ?: EMPTY_STRING,
                        it.email ?: EMPTY_STRING,
                        it.postId ?: EMPTY_STRING,
                        it.name ?: EMPTY_STRING,
                        it.body ?: EMPTY_STRING,
                    )
                }
            }

            override suspend fun updateDatabase(
                data: List<CommentEntity>,
                info: CommentInfo
            ): Boolean {
                localSource.updateAll(data)
                return true
            }

            override suspend fun readFromDatabase(info: CommentInfo): Flow<List<CommentEntity>> {
                return localSource.getAll()
            }

            override fun getErrorHandler() = errorHandler
        }

        return operation.execute(infoItem)
    }

    suspend fun getById(commentId: String): Optional<CommentEntity> {
        return localSource.getById(commentId)
    }

    suspend fun updateByID(commentId: String) {
        val currentValue = getById(commentId)
        if (currentValue is Optional.Some) {
            localSource.createOrUpdate(currentValue.element)
        }
    }
}


sealed class CommentInfo(
    val requiresRemote: Boolean,
    val timeOperation: Long
)

class GetCommentInfo(
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : CommentInfo(
    requiresRemote,
    timeOperation,
)

class ItemCommentInfo(
    val item: CommentEntity,
    val increment: Boolean = true,
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : CommentInfo(
    requiresRemote,
    timeOperation,
)

class MultipleItemsCommentInfo(
    val items: List<CommentEntity>,
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : CommentInfo(
    requiresRemote,
    timeOperation,
)
