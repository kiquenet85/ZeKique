package com.example.zemogatest.data.user.repository

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.repository.base.operation.RepositoryReadOperation
import com.example.zemogatest.data.db.entity.UserEntity
import com.example.zemogatest.data.user.sources.UserLocalSource
import com.example.zemogatest.data.user.sources.UserRemoteSource
import com.example.zemogatest.domain.remote.UserResponseDTO
import com.example.zemogatest.util.EMPTY_STRING
import com.example.zemogatest.util.Optional
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localSource: UserLocalSource,
    private val remoteSource: UserRemoteSource,
    private val errorHandler: ErrorHandler
) {

    suspend fun getAll(infoItem: UserInfo): Flow<List<UserEntity>> {
        val operation = object :
            RepositoryReadOperation<List<UserResponseDTO>, List<UserEntity>, UserInfo, List<UserEntity>> {

            override suspend fun shouldGoRemote(info: UserInfo): Boolean {
                return infoItem.requiresRemote
            }

            override suspend fun endpoint(info: UserInfo): List<UserResponseDTO> {
                return remoteSource.getAll()
            }

            override suspend fun transformRemoteResult(
                remoteData: List<UserResponseDTO>,
                info: UserInfo
            ): List<UserEntity> {
                return remoteData.map {
                    UserEntity(
                        it.id ?: EMPTY_STRING,
                        it.name ?: EMPTY_STRING,
                        it.email ?: EMPTY_STRING,
                        it.phone ?: EMPTY_STRING,
                        it.website ?: EMPTY_STRING
                    )
                }
            }

            override suspend fun updateDatabase(
                data: List<UserEntity>,
                info: UserInfo
            ): Boolean {
                localSource.updateAll(data)
                return true
            }

            override suspend fun readFromDatabase(info: UserInfo): Flow<List<UserEntity>> {
                return localSource.getAll()
            }

            override fun getErrorHandler() = errorHandler
        }

        return operation.execute(infoItem)
    }

    suspend fun getById(userId: String): Optional<UserEntity> {
        return localSource.getById(userId)
    }

    suspend fun updateByID(userId: String) {
        val currentValue = getById(userId)
        if (currentValue is Optional.Some) {
            localSource.createOrUpdate(currentValue.element)
        }
    }
}


sealed class UserInfo(
    val requiresRemote: Boolean,
    val timeOperation: Long
)

class GetUserInfo(
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : UserInfo(
    requiresRemote,
    timeOperation,
)

class ItemUserInfo(
    val item: UserEntity,
    val increment: Boolean = true,
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : UserInfo(
    requiresRemote,
    timeOperation,
)

class MultipleItemsUserInfo(
    val items: List<UserEntity>,
    requiresRemote: Boolean = true,
    timeOperation: Long = System.currentTimeMillis()
) : UserInfo(
    requiresRemote,
    timeOperation,
)
