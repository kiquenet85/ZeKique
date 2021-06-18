package com.example.zemogatest.presentation.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.manager.ResourceManager
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.data.comment.use_case.LoadCommentsUC
import com.example.zemogatest.data.post.use_case.UpdateFavoritePostUC
import com.example.zemogatest.presentation.base.BaseCoroutineViewModel
import com.example.zemogatest.presentation.post.detail.ui_model.CommentUI
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    resourceManager: ResourceManager,
    errorHandler: ErrorHandler,
    networkManager: NetworkManager,
    private val loadCommentsUC: LoadCommentsUC,
    private val updatePostFavoriteUC: UpdateFavoritePostUC,
) : BaseCoroutineViewModel(resourceManager, errorHandler, networkManager) {

    var post: PostUI? = state[EXTRA_SELECTED_USER]
    private val screenState = MutableLiveData<CommentUIState>(CommentUIState.CommentLoading)

    fun getScreenState(): LiveData<CommentUIState> = screenState

    fun loadComments(refreshFromNetwork: Boolean) {
        viewModelScope.launch(coroutineErrorHandler) {
            loadCommentsUC.execute(refreshFromNetwork, post).collect {
                screenState.value = it
            }
        }
    }

    fun setSelectedPost(postUI: PostUI?) {
        post = postUI
        state.set(EXTRA_SELECTED_USER, post)
    }

    fun addPostAsFavorite(value: Boolean) {
        post?.let {
            viewModelScope.launch(coroutineErrorHandler) {
                updatePostFavoriteUC.execute(it, value)
            }
        }
    }

    companion object {
        const val EXTRA_SELECTED_USER = "EXTRA_SELECTED_USER"
    }
}

sealed class CommentUIState {
    object CommentLoading : CommentUIState()
    object CommentEmpty : CommentUIState()
    class CommentLoaded(val value: List<CommentUI>) : CommentUIState()
}
