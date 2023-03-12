package com.antisoftware.popreminder.common.workmanager

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.common.snackbar.SnackbarMessage
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.screens.edit.EditReminderViewModel
import com.antisoftware.popreminder.screens.reminders.ReminderViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import com.antisoftware.popreminder.R.drawable as AppIcon


object Notifications {
    lateinit var appContext: Context

    fun setContext(context: Context) {
        appContext = context
    }

    fun createOneTimeNotification(
        duration: Duration,
        title: String,
        content: String,
        reminder: MutableState<Reminder>,
        viewModel: EditReminderViewModel
    ) {
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
                    viewModel.updateDone(reminder.value)
                }
            }
    }

    fun createNotification(
        title: String,
        content: String,
        icon: Int = AppIcon.pop_icon_final
    ) {
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

    fun setNotification(reminder: MutableState<Reminder>, viewModel: EditReminderViewModel) {
        var duration by mutableStateOf(Duration.ZERO)
        val currentTimeMillis = System.currentTimeMillis()
        val notificationMillis = reminder.value.dueMillis
        if (notificationMillis > currentTimeMillis) {
            duration = Duration.ofMillis(notificationMillis - currentTimeMillis)
        }

        Notifications.createOneTimeNotification(
            duration = duration,
            title = setTitle(reminder),
            content = "Check the PopReminder app for details.",
            reminder,
            viewModel
        )
        SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(durationToString(duration)))
    }

    fun setTitle(reminder: MutableState<Reminder>): String {
        val title = reminder.value.msg
        return "Your '$title' reminder is due!"
    }

    fun durationToString(duration: Duration): String {
        var finalString by mutableStateOf("")
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        if (days > 0) {
            finalString = "There is $days day(s), $hours hour(s), and $minutes minute(s) until the notification."
        } else if (hours > 0) {
            finalString = "There is $hours hour(s), and $minutes minute(s) until the notification."
        } else {
            finalString = "There is $minutes minute(s) until the notification."
        }
        return finalString
    }
}