package com.antisoftware.popreminder.screens.edit

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.antisoftware.popreminder.R.drawable as AppIcon
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.common.composable.*
import com.antisoftware.popreminder.common.extension.card
import com.antisoftware.popreminder.common.extension.fieldModifier
import com.antisoftware.popreminder.common.extension.spacer
import com.antisoftware.popreminder.common.extension.toolbarActions
import com.antisoftware.popreminder.data.Reminder
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

@Composable
@ExperimentalMaterialApi
fun EditReminderScreen(
    popUpScreen: () -> Unit,
    reminderId: String,
    modifier: Modifier = Modifier,
    viewModel: EditReminderViewModel = hiltViewModel()
) {
    val reminder by viewModel.reminder

    LaunchedEffect(Unit) { viewModel.initialize(reminderId) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.edit_reminder,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(popUpScreen) },
            backAction = { viewModel.onBackClick(popUpScreen) }
        )

        Spacer(modifier = Modifier.spacer())

        BasicField(AppText.reminder, reminder.msg, viewModel::onTitleChange, Modifier.fieldModifier())
        DescriptionField(AppText.description, reminder.description, viewModel::onDescriptionChange, Modifier.fieldModifier())

        Spacer(modifier = Modifier.spacer())
        CardEditors(reminder, viewModel::onDateChange, viewModel::onTimeChange)

        Spacer(modifier = Modifier.spacer())
    }
}

@ExperimentalMaterialApi
@Composable
private fun CardEditors(
    reminder: Reminder,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit
) {
    val activity = LocalContext.current as AppCompatActivity

    RegularCardEditor(AppText.date, AppIcon.ic_calendar, reminder.dueDate, Modifier.card()) {
        showDatePicker(activity, onDateChange)
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, reminder.dueTime, Modifier.card()) {
        showTimePicker(activity, onTimeChange)
    }
}

private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
    }
}

private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
    val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
    }
}
