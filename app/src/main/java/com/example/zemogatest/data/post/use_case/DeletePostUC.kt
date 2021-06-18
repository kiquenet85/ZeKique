package com.example.zemogatest.data.post.use_case

import com.example.zemogatest.data.post.repository.PostRepository
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeletePostUC @Inject constructor(
    private val postRepository: PostRepository
) {

    //TODO inject the dispatchers via hilt
    suspend fun execute(post: PostUI) = withContext(Dispatchers.Default) {
        postRepository.delete(post.id)
        return@withContext true
    }
}