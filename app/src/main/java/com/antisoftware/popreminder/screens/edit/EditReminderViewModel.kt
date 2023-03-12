package com.antisoftware.popreminder.screens.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.antisoftware.popreminder.MAP_SCREEN
import com.antisoftware.popreminder.REMINDER_DEFAULT_ID
import com.antisoftware.popreminder.REMINDER_ID
import com.antisoftware.popreminder.REMINDER_ID_ARG
import com.antisoftware.popreminder.common.extension.idFromParameter
import com.antisoftware.popreminder.common.workmanager.Notifications
import com.antisoftware.popreminder.data.LocationObject
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.Spot
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import com.google.android.gms.maps.model.LatLng
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
                reminder.value =
                    storageService.getReminder(reminderId.idFromParameter()) ?: Reminder()
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
            Notifications.setNotification(reminder, this@EditReminderViewModel)
            popUpScreen()
        }
    }

    fun onMapsClick(openScreen: (String) -> Unit) {
        launchCatching {
            val editedTask = reminder.value
            if (editedTask.id.isBlank()) {
                storageService.save(editedTask)
            } else {
                storageService.update(editedTask)
            }
        }
        openScreen(MAP_SCREEN)
    }


    fun updateDone(reminder: Reminder) {
        launchCatching { storageService.update(reminder.copy(dueDateGone = true)) }
    }

    fun onBackClick(popUpScreen: () -> Unit) {
        popUpScreen()
    }

    fun setRemainingValues() {
        if (reminder.value.creation_time.isBlank()) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val currentTime = LocalDateTime.now().format(formatter)
            reminder.value = reminder.value.copy(creation_time = currentTime)
        }
        val dueMillis = reminder.value.dueDateInMillis + reminder.value.dueTimeInMillis
        reminder.value = reminder.value.copy(dueMillis = dueMillis)
        if (LocationObject.location != LatLng(0.0, 0.0)) {
            reminder.value = reminder.value.copy(
                latitude = LocationObject.location.latitude,
                longitude = LocationObject.location.longitude
            )
        }
    }

        private fun Int.toClockPattern(): String {
            return if (this < 10) "0$this" else "$this"
    }


    companion object {
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
    }

    fun onMapLongClick(location: LatLng) {
        LocationObject.location = location
    }

    fun onMarkerLongClick() {
        reminder.value = reminder.value.copy(latitude = null, longitude = null)
    }

    fun onOkClick(popUpScreen: () -> Unit) {
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
}
