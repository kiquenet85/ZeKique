package com.example.zemogatest.data.post.use_case

import com.example.zemogatest.data.post.repository.GetPostInfo
import com.example.zemogatest.data.post.repository.PostRepository
import com.example.zemogatest.data.user.repository.GetUserInfo
import com.example.zemogatest.data.user.repository.UserRepository
import com.example.zemogatest.presentation.post.list.PostUIState
import com.example.zemogatest.presentation.post.list.PostViewModel
import com.example.zemogatest.presentation.post.list.state.PostUI
import com.example.zemogatest.presentation.post.list.state.UserUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadPostsUC @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    //TODO inject the dispatchers via hilt
    suspend fun execute(
        refreshFromNetwork: Boolean,
        filter: PostViewModel.Filter
    ): Flow<PostUIState> = withContext(Dispatchers.Default) {

        val deferredJobUsers = async {
            userRepository.getAll(GetUserInfo(refreshFromNetwork))
        }

        postRepository.getAll(GetPostInfo(refreshFromNetwork))
            .map { list ->
                var notSeenCounter = 20
                val listPostUI = list.map {
                    val uiItem =
                        PostUI(
                            it.id,
                            it.title,
                            it.body,
                            it.favorite,
                            it.userId,
                            it.seen,
                            !it.seen && notSeenCounter > 0
                        )
                    if (!it.seen) notSeenCounter--
                    uiItem
                }

                if (PostViewModel.Filter.FAVORITES == filter) {
                    listPostUI.filter { it.favorite }
                } else listPostUI
            }.combine(deferredJobUsers.await()) { posts, users ->
                if (posts.isNotEmpty()) {
                    val mapUsers = users.groupBy { it.id }
                    posts.map {
                        mapUsers[it.userId]?.first()?.let { user ->
                            it.copy(
                                userUI = UserUI(
                                    user.name,
                                    user.email,
                                    user.phone,
                                    user.website
                                )
                            )
                        } ?: it
                    }
                } else listOf()
            }.map { resultList ->
                if (resultList.isEmpty()) {
                    PostUIState.PostEmpty
                } else {
                    PostUIState.PostLoaded(resultList)
                }
            }
    }
}