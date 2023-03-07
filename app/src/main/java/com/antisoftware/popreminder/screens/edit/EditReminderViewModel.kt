package com.antisoftware.popreminder.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.antisoftware.popreminder.REMINDER_DEFAULT_ID
import com.antisoftware.popreminder.common.extension.idFromParameter
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.common.snackbar.SnackbarMessage
import com.antisoftware.popreminder.common.workmanager.Notifications
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditReminderViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
) : PopReminderViewModel(logService) {
    val reminder = mutableStateOf(Reminder())

    fun initialize(reminderId: String) {
        launchCatching {
            if (reminderId != REMINDER_DEFAULT_ID) {
                reminder.value = storageService.getReminder(reminderId.idFromParameter()) ?: Reminder()
            }
        }
    }

    fun onTitleChange(newValue: String) {
        reminder.value = reminder.value.copy(msg = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        reminder.value = reminder.value.copy(description = newValue)
    }

    fun onDateChange(newValue: Long) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = newValue
        val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
        reminder.value = reminder.value.copy(dueDate = newDueDate)
        reminder.value = reminder.value.copy(dueDateInMillis = newValue)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val offset = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000 / 60 / 60
        val millis = ((hour - offset) * 3600000) + (minute * 60000).toLong()
        val newDueTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
        reminder.value = reminder.value.copy(dueTime = newDueTime)
        reminder.value = reminder.value.copy(dueTimeInMillis = millis)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        setRemainingValues()
        launchCatching {
            val editedTask = reminder.value
            if (editedTask.id.isBlank()) {
                storageService.save(editedTask)
            } else {
                storageService.update(editedTask)
            }
            setNotification()
            popUpScreen()
        }
    }

    fun onBackClick(popUpScreen: () -> Unit) {
        popUpScreen()
    }

    fun setNotification() {
        var duration by mutableStateOf(Duration.ZERO)
        val currentTimeMillis = System.currentTimeMillis()
        val notificationMillis = reminder.value.dueMillis
        if (notificationMillis > currentTimeMillis) {
            duration = Duration.ofMillis(notificationMillis - currentTimeMillis)
        }

        Notifications.createOneTimeNotification(duration = duration, title = setTitle(), content = "Check the PopReminder app for details.")
        SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(durationToString(duration)))
    }

    fun setTitle(): String {
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

    fun setRemainingValues() {
        if (reminder.value.creation_time.isBlank()) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val currentTime = LocalDateTime.now().format(formatter)
            reminder.value = reminder.value.copy(creation_time = currentTime)
        }
        val dueMillis = reminder.value.dueDateInMillis + reminder.value.dueTimeInMillis
        reminder.value = reminder.value.copy(dueMillis = dueMillis)
    }

        private fun Int.toClockPattern(): String {
            return if (this < 10) "0$this" else "$this"
    }


    companion object {
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
    }
}
