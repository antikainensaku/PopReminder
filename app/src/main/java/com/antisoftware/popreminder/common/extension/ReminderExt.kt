package com.antisoftware.popreminder.common.extension

import com.antisoftware.popreminder.data.Reminder

fun Reminder?.hasDueDate(): Boolean {
    return this?.dueDate.orEmpty().isNotBlank()
}

fun Reminder?.hasDueTime(): Boolean {
    return this?.dueTime.orEmpty().isNotBlank()
}
