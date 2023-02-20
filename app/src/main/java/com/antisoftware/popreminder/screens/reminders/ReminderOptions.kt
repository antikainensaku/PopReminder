package com.antisoftware.popreminder.screens.reminders

enum class ReminderOptions(val message: String) {
    EditReminder("Edit reminder"),
    DeleteReminder("Delete reminder");

    companion object {
        fun getByMessage(msg: String): ReminderOptions {
            values().forEach { action -> if (msg == action.message) return action }

            return EditReminder
        }

        fun getOptions(hasEditOption: Boolean): List<String> {
            val options = mutableListOf<String>()
            values().forEach { reminderAction ->
                if (hasEditOption || reminderAction != EditReminder) {
                    options.add(reminderAction.message)
                }
            }
            return options
        }
    }
}
