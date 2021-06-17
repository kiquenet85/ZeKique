@file:Suppress("UNCHECKED_CAST")

package com.example.zemogatest.common.repository.base.operation

import com.example.zemogatest.common.repository.base.RepositoryPolicy
import com.example.zemogatest.common.error.ErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge

interface RepositoryReadOperation<Remote, Local, Info, Return> : RepositoryPolicy<Info> {

    fun getErrorHandler(): ErrorHandler

    @ExperimentalCoroutinesApi
    suspend fun execute(info: Info): Flow<Return> {
        val emmitRemoteErrors = flow<Return> {
            if (shouldGoRemote(info)) {
                val remoteResult = endpoint(info)
                val transformedResult = transformRemoteResult(remoteResult, info)
                updateDatabase(transformedResult, info)
            }
        }.catch { e ->
            getErrorHandler().report(e)
        }.flowOn(Dispatchers.IO)
        return (listOf(readFromDatabase(info), emmitRemoteErrors).merge())
    }

    suspend fun endpoint(info: Info): Remote = info as Remote
    suspend fun transformRemoteResult(remoteData: Remote, info: Info): Local =
        remoteData as Local

    suspend fun updateDatabase(data: Local, info: Info): Boolean = false
    suspend fun readFromDatabase(info: Info): Flow<Return>
}
