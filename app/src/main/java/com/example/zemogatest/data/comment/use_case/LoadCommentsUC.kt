package com.example.zemogatest.data.comment.use_case

import com.example.zemogatest.data.comment.repository.CommentRepository
import com.example.zemogatest.data.comment.repository.GetCommentInfo
import com.example.zemogatest.presentation.post.detail.CommentUIState
import com.example.zemogatest.presentation.post.detail.ui_model.CommentUI
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadCommentsUC @Inject constructor(
    private val commentRepository: CommentRepository
) {

    //TODO inject the dispatchers via hilt
    suspend fun execute(
        refreshFromNetwork: Boolean,
        post: PostUI?,
    ): Flow<CommentUIState> = withContext(Dispatchers.Default) {
        commentRepository.getAll(GetCommentInfo(refreshFromNetwork))
            .map { resultList ->
                if (resultList.isEmpty()) {
                    CommentUIState.CommentEmpty
                } else {
                    val commentUIList = resultList.filter { it.postId == post?.id }.map {
                        CommentUI(it.id, it.name, it.body, post)
                    }
                    CommentUIState.CommentLoaded(commentUIList)
                }
            }
    }
}