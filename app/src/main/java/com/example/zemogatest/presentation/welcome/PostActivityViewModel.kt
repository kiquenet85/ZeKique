package com.example.zemogatest.presentation.welcome

import androidx.lifecycle.viewModelScope
import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.manager.ResourceManager
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.data.db.use_case.DeleteAllTablesUC
import com.example.zemogatest.presentation.base.BaseCoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostActivityViewModel @Inject constructor(
    resourceManager: ResourceManager,
    errorHandler: ErrorHandler,
    networkManager: NetworkManager,
    private val deleteAllTablesUC: DeleteAllTablesUC
) : BaseCoroutineViewModel(resourceManager, errorHandler, networkManager) {

    fun deleteAll() {
        viewModelScope.launch(coroutineErrorHandler) {
            deleteAllTablesUC.execute()
        }
    }
}
