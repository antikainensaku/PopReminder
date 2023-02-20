package com.antisoftware.popreminder.screens.reminders

import androidx.compose.runtime.mutableStateOf
import com.antisoftware.popreminder.data.firebase.ConfigurationService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.EDIT_REMINDER_SCREEN
import com.antisoftware.popreminder.PROFILE_SCREEN
import com.antisoftware.popreminder.REMINDER_ID
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : PopReminderViewModel(logService) {
    val options = mutableStateOf<List<String>>(listOf())

    val reminders = storageService.reminders

    fun loadReminderOptions() {
        val hasEditOption = configurationService.isShowTaskEditButtonConfig
        options.value = ReminderOptions.getOptions(hasEditOption)
    }

    fun onReminderCheckChange(reminder: Reminder) {
        launchCatching { storageService.update(reminder.copy(completed = !reminder.completed)) }
    }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_REMINDER_SCREEN)

    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)

    fun onReminderActionClick(openScreen: (String) -> Unit, reminder: Reminder, action: String) {
        when (ReminderOptions.getByMessage(action)) {
            ReminderOptions.EditReminder -> openScreen("$EDIT_REMINDER_SCREEN?$REMINDER_ID={${reminder.id}}")
            ReminderOptions.DeleteReminder -> onDeleteReminderClick(reminder)
        }
    }

    private fun onDeleteReminderClick(reminder: Reminder) {
        launchCatching { storageService.delete(reminder.id) }
    }
}
