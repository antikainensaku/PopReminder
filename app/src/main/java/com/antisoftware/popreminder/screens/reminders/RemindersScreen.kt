package com.antisoftware.popreminder.screens.reminders

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antisoftware.popreminder.common.composable.ActionToolbar
import com.antisoftware.popreminder.common.extension.smallSpacer
import com.antisoftware.popreminder.common.extension.toolbarActions
import com.antisoftware.popreminder.R.drawable as AppIcon
import com.antisoftware.popreminder.R.string as AppText

@OptIn(ExperimentalLifecycleComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun RemindersScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddClick(openScreen) },
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) {
        val reminders = viewModel.reminders.collectAsStateWithLifecycle(emptyList())
        // in case notification hasn't happened and dueDate has passed
        if (reminders.value.size > 1) {
            for (i in 0..reminders.value.size - 1) {
                if (!reminders.value[i].dueDateGone && reminders.value[i].dueMillis <= System.currentTimeMillis()) {
                    viewModel.updateDueDateGone(reminders.value[i])
                }
            }
        }
        val options by viewModel.options
        var showAll by rememberSaveable { mutableStateOf(false) }
        var show by rememberSaveable {
            mutableStateOf(false)
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            ActionToolbar(
                title = AppText.reminders,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_user,
                checked = showAll,
                onCheckChange = { showAll = showAll.not() },
                endAction = { viewModel.onProfileClick(openScreen) }
            )

            Spacer(modifier = Modifier.smallSpacer())

            LazyColumn {
                items(reminders.value, key = { it.id }) { reminderItem ->
                    if (reminderItem.dueDateGone) { show = true }
                    ReminderItem(
                        reminder = reminderItem,
                        options = options,
                        showAll = showAll,
                        onCheckChange = { viewModel.onReminderCheckChange(reminderItem) },
                        onActionClick = { action -> viewModel.onReminderActionClick(openScreen, reminderItem, action) }
                    )
                    show = false
                }
            }
        }
    }

    LaunchedEffect(viewModel) { viewModel.loadReminderOptions() }
}