package com.antisoftware.popreminder.common.workmanager

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.time.Duration
import java.util.concurrent.TimeUnit
import com.antisoftware.popreminder.R.drawable as AppIcon


object Notifications {
    lateinit var appContext: Context

    fun setContext(context: Context) {
        appContext = context
    }

    fun createOneTimeNotification(delay: Long, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        val workManager = WorkManager.getInstance(appContext)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()
        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(duration = delay, timeUnit = timeUnit)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever {workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    createNotification("Test notification title", "This is a test notification for PopReminder App")
                }
            }
    }

    fun createOneTimeNotification(duration: Duration, title: String, content: String) {
        val workManager = WorkManager.getInstance(appContext)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()
        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(duration)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever {workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    createNotification(title, content)
                }
            }
    }

    fun createNotification(title: String, content: String, icon: Int = AppIcon.pop_icon_final) {
        val notificationBuilder : NotificationCompat.Builder =
            NotificationCompat.Builder(appContext, "CHANNEL_ID")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notification : Notification = notificationBuilder.build()
        with(NotificationManagerCompat.from(appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("NotificationWorker", "Permission Failed")
            }
            notify(1, notification)
        }
    }
}