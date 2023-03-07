package com.antisoftware.popreminder.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.antisoftware.popreminder.common.workmanager.Notifications
import com.antisoftware.popreminder.data.firebase.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class PopReminderViewModel(private val logService: LogService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
    init {
        createNotificationChannel(context = Notifications.appContext)
    }
}

private fun createNotificationChannel(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val channel = NotificationChannel(
            "CHANNEL_ID",
            "Default notification channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Default notification channel"
        }
        notificationManager.createNotificationChannel(channel)
    }
}
