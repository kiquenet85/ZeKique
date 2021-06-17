package com.example.zemogatest.data.post.repository

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.repository.base.operation.RepositoryReadOperation
import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.data.post.sources.PostLocalSource
import com.example.zemogatest.data.post.sources.PostRemoteSource
import com.example.zemogatest.domain.remote.PostResponseDTO
import com.example.zemogatest.util.EMPTY_STRING
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val localSource: PostLocalSource,
    private val remoteSource: PostRemoteSource,
    private val errorHandler: ErrorHandler
) {

    suspend fun getAll(infoItem: PostInfo): Flow<List<PostEntity>> {
        val operation = object :
            RepositoryReadOperation<List<PostResponseDTO>, List<PostEntity>, PostInfo, List<PostEntity>> {

            override suspend fun shouldGoRemote(info: PostInfo): Boolean {
                return infoItem.requiresRemote
            }

            override suspend fun endpoint(info: PostInfo): List<PostResponseDTO> {
                return remoteSource.getAll()
            }

            override suspend fun transformRemoteResult(
                remoteData: List<PostResponseDTO>,
                info: PostInfo
            ): List<PostEntity> {
                return remoteData.map {
                    PostEntity(
                        it.id ?: EMPTY_STRING,
                        it.userId ?: EMPTY_STRING,
                        it.title ?: EMPTY_STRING,
                        it.body ?: EMPTY_STRING,
                        false
                    )
                }
            }

            override suspend fun updateDatabase(
                data: List<PostEntity>,
                info: PostInfo
            ): Boolean {
                val mapCurrentValues = localSource.getAll().filter { it.favorite }.groupBy { it.id }
                val valuesToUpdate = if (mapCurrentValues.isNotEmpty()) {
                    data.map {
                        mapCurrentValues[it.id]?.first()?.let { currentValue ->
                            if (currentValue.favorite){
                                return@map it.copy(favorite = true)
                            }
                        }
                        it
                    }
                } else data
                localSource.updateAll(valuesToUpdate)
                return true
            }

            override suspend fun readFromDatabase(info: PostInfo): Flow<List<PostEntity>> {
                return localSource.getAllAndObserve()
            }

            override fun getErrorHandler() = errorHandler
        }

        return operation.execute(infoItem)
    }

    suspend fun getById(postId: String): Optional<PostEntity> {
        return localSource.getById(postId)
    }

    suspend fun updateByID(postId: String) {
        val currentValue = getById(postId)
        if (currentValue is Optional.Some) {
            localSource.createOrUpdate(currentValue.element)
        }
    }
}


sealed class PostInfo(
    val requiresRemote: Boolean,
    val timeOperation: Long
)

class GetPostInfo(
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : PostInfo(
    requiresRemote,
    timeOperation,
)

class ItemPostInfo(
    val item: PostEntity,
    val increment: Boolean = true,
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : PostInfo(
    requiresRemote,
    timeOperation,
)

class MultipleItemsPostInfo(
    val items: List<PostEntity>,
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : PostInfo(
    requiresRemote,
    timeOperation,
)
