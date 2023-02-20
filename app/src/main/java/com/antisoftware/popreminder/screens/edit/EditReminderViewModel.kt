package com.antisoftware.popreminder.screens.edit

import androidx.compose.runtime.mutableStateOf
import com.antisoftware.popreminder.REMINDER_DEFAULT_ID
import com.antisoftware.popreminder.common.extension.idFromParameter
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
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
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
        calendar.timeInMillis = newValue
        val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
        reminder.value = reminder.value.copy(dueDate = newDueDate)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val newDueTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
        reminder.value = reminder.value.copy(dueTime = newDueTime)
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
            popUpScreen()
        }
    }

    fun onBackClick(popUpScreen: () -> Unit) {
        popUpScreen()
    }

    fun setRemainingValues() {
        if (reminder.value.creation_time.isBlank()) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val current_time = LocalDateTime.now().format(formatter)
            reminder.value = reminder.value.copy(creation_time = current_time)
        }
    }

    private fun Int.toClockPattern(): String {
        return if (this < 10) "0$this" else "$this"
    }


    companion object {
        private const val UTC = "UTC"
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
    }
}
