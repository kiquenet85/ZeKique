package com.example.zemogatest.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zemogatest.R
import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.common.manager.ResourceManager
import com.example.zemogatest.common.network.NetworkManager
import com.example.zemogatest.util.EMPTY_STRING
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

abstract class BaseCoroutineViewModel(
    protected val resourceManager: ResourceManager,
    protected val errorHandler: ErrorHandler,
    protected val networkManager: NetworkManager
) : ViewModel() {
    open val coroutineErrorHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            try {
                when {
                    !networkManager.isOnline -> {
                        errorState.value =
                            CommonErrorState.ConnectionError(resourceManager.getString(R.string.connection_error_description))
                    }
                    else -> {
                        errorState.value =
                            CommonErrorState.CriticalUIError(resourceManager.getString(R.string.general_error_handled))
                        errorHandler.reportCriticalException(
                            exception,
                            "Handled exception on coroutine builder"
                        )
                    }
                }
            } catch (reportException: Exception) {
                val message = "Error handling exception on coroutine error handler"
                errorState.value = CommonErrorState.CriticalUIError(message)
                errorHandler.reportErrorHandlingException(exception, message)
            }
        }
    }

    private val errorState: MutableLiveData<CommonErrorState> = MutableLiveData()
    fun getErrorState(): LiveData<CommonErrorState> = errorState
}

sealed class CommonErrorState(open val message: String) {
    class LogicalError(val originalException: Exception) :
        CommonErrorState(originalException.message ?: EMPTY_STRING)

    class ConnectionError(override val message: String) : CommonErrorState(message)
    class CriticalUIError(override val message: String) : CommonErrorState(message)
}
