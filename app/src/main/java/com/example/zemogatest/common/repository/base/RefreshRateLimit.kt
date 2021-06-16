package com.cornershop.counterstest.common.repository.base

import android.text.format.DateUtils
import kotlin.math.abs

interface RefreshRateLimit {
    fun shouldRefreshData(timeLastRemoteOperations: Long, newOperationTime: Long): Boolean {
        return if (timeLastRemoteOperations == NO_DIFF_TIME) {
            true
        } else {
            val currentTime = System.currentTimeMillis()
            val differenceSeconds: Long =
                (timeLastRemoteOperations - currentTime) / DateUtils.SECOND_IN_MILLIS
            abs(differenceSeconds) >= TIME_TO_NEXT_REMOTE_AFTER_SECONDS
        }
    }

    companion object {
        const val TIME_TO_NEXT_REMOTE_AFTER_SECONDS = 20
        const val NO_DIFF_TIME = 0L
    }
}
