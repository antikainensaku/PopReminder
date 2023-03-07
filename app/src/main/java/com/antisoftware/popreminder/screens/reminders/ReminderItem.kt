package com.antisoftware.popreminder.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antisoftware.popreminder.common.composable.DropdownContextMenu
import com.antisoftware.popreminder.common.extension.contextMenu
import com.antisoftware.popreminder.common.extension.divider
import com.antisoftware.popreminder.common.extension.hasDueDate
import com.antisoftware.popreminder.common.extension.hasDueTime
import com.antisoftware.popreminder.data.Reminder
import java.lang.StringBuilder

@Composable
@ExperimentalMaterialApi
fun ReminderItem(
    reminder: Reminder,
    options: List<String>,
    showAll: Boolean,
    onCheckChange: () -> Unit,
    onActionClick: (String) -> Unit
) {
    if (reminder.dueMillis < System.currentTimeMillis() || showAll) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = reminder.completed,
                onCheckedChange = { onCheckChange() },
                modifier = Modifier.padding(8.dp, 0.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = reminder.msg, style = MaterialTheme.typography.subtitle2)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = getDueDateAndTime(reminder), fontSize = 12.sp)
                }
            }

            DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
        }
        Divider(Modifier.divider())
    }
}

private fun getDueDateAndTime(reminder: Reminder): String {
    val stringBuilder = StringBuilder("")

    if (reminder.hasDueDate()) {
        stringBuilder.append(reminder.dueDate)
        stringBuilder.append(" ")
    }

    if (reminder.hasDueTime()) {
        stringBuilder.append("at ")
        stringBuilder.append(reminder.dueTime)
    }

    return stringBuilder.toString()
}
