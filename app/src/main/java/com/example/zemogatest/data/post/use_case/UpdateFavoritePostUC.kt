package com.example.zemogatest.data.post.use_case

import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.data.post.repository.PostRepository
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFavoritePostUC @Inject constructor(
    private val postRepository: PostRepository
) {

    //TODO inject the dispatchers via hilt
    suspend fun execute(post: PostUI, value: Boolean) = withContext(Dispatchers.Default) {
        postRepository.updateByID(
            PostEntity(
                post.id,
                post.userId,
                post.title,
                post.description,
                value,
                true
            )
        )
        return@withContext true
    }
}