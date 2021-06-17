package com.example.zemogatest.presentation.post.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.manager.ResourceManager
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.presentation.base.BaseCoroutineViewModel
import com.example.zemogatest.presentation.post.list.state.PostUI
import com.example.zemogatest.presentation.post.list.use_case.LoadPostsUC
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
) : BaseCoroutineViewModel(resourceManager, errorHandler, networkManager) {

    private val screenState = MutableLiveData<PostUIState>(PostUIState.PostLoading)

    fun getScreenState() : LiveData<PostUIState> = screenState

    fun loadPosts(refreshFromNetwork: Boolean){
        viewModelScope.launch(coroutineErrorHandler) {
            loadPostsUC.execute(refreshFromNetwork).collect {
                screenState.value = it
            }
        }
    }
}

sealed class PostUIState{
    object PostLoading : PostUIState()
    object PostEmpty: PostUIState()
    class PostLoaded(val value: List<PostUI>) : PostUIState()
}

