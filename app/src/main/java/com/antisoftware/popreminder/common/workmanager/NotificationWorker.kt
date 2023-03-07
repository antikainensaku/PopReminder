package com.antisoftware.popreminder.common.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): Worker(context, workerParams) {
    override fun doWork(): Result {
        return try {
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}