package com.example.zemogatest.presentation.post.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.manager.ResourceManager
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.data.post.use_case.DeletePostUC
import com.example.zemogatest.data.post.use_case.LoadPostsUC
import com.example.zemogatest.data.post.use_case.UpdateSeenPostUC
import com.example.zemogatest.presentation.base.BaseCoroutineViewModel
import com.example.zemogatest.presentation.base.Event
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val state: SavedStateHandle,
    resourceManager: ResourceManager,
    errorHandler: ErrorHandler,
    networkManager: NetworkManager,
    private val loadPostsUC: LoadPostsUC,
    private val updatePostsUC: UpdateSeenPostUC,
    private val deletePostUC: DeletePostUC
) : BaseCoroutineViewModel(resourceManager, errorHandler, networkManager) {

    enum class Filter {
        NONE, FAVORITES
    }

    var filter = Filter.NONE
    private val screenState = MutableLiveData<PostUIState>(PostUIState.PostLoading)
    private val screenEvent = MutableLiveData<Event<PostUIEvent>>()

    fun getScreenState(): LiveData<PostUIState> = screenState
    fun getScreenEvent(): LiveData<Event<PostUIEvent>> = screenEvent

    fun loadPosts(refreshFromNetwork: Boolean) {
        if (refreshFromNetwork) screenState.value = PostUIState.PostLoading
        viewModelScope.launch(coroutineErrorHandler) {
            loadPostsUC.execute(refreshFromNetwork, filter).collect {
                screenState.value = it
            }
        }
    }

    fun updatePostAsSeen(updatedPost: PostUI) {
        viewModelScope.launch(coroutineErrorHandler) {
            updatePostsUC.execute(updatedPost)
            screenEvent.value = Event(PostUIEvent.NavigateToDetail(updatedPost))
        }
    }

    fun deletePost(postUI: PostUI) {
        viewModelScope.launch(coroutineErrorHandler) {
            deletePostUC.execute(postUI)
        }
    }
}

sealed class PostUIState {
    object PostLoading : PostUIState()
    object PostEmpty : PostUIState()
    class PostLoaded(val value: List<PostUI>) : PostUIState()
}

sealed class PostUIEvent {
    class NavigateToDetail(val postUI: PostUI) : PostUIEvent()
}
