package com.example.zemogatest.data.db.use_case

import com.example.zemogatest.data.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteAllTablesUC @Inject constructor(
    private val userRepository: UserRepository
) {

    //TODO inject the dispatchers via hilt
    suspend fun execute() = withContext(Dispatchers.Default) {
        return@withContext userRepository.deleteAll()
    }
}