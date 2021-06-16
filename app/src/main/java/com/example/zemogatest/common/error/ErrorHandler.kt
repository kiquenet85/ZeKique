package com.example.zemogatest.common.error

import android.util.Log

/**
 * Just report the error to the corresponding place.
 */
class ErrorHandler {

    private val TAG = ErrorHandler::class.java.canonicalName

    fun report(throwable: Throwable) {
        Log.e(TAG, "Reporting Exception", throwable)
    }

    fun reportCriticalException(throwable: Throwable, message: String) {
        Log.e(TAG, "Reporting CRITICAL Exception: \\n".plus(message), throwable)
    }

    fun reportErrorHandlingException(throwable: Throwable, message: String) {
        Log.e(
            TAG,
            "Reporting CRITICAL Exception when handling throwable: \\n".plus(message),
            throwable
        )
    }
}