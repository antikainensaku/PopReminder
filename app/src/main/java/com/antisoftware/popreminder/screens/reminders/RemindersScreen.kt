package com.antisoftware.popreminder.screens.reminders

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antisoftware.popreminder.R.drawable as AppIcon
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.common.composable.ActionToolbar
import com.antisoftware.popreminder.common.extension.smallSpacer
import com.antisoftware.popreminder.common.extension.toolbarActions

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
        val options by viewModel.options

        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            ActionToolbar(
                title = AppText.reminders,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_user,
                endAction = { viewModel.onProfileClick(openScreen) }
            )

            Spacer(modifier = Modifier.smallSpacer())

            LazyColumn {
                items(reminders.value, key = { it.id }) { reminderItem ->
                    ReminderItem(
                        reminder = reminderItem,
                        options = options,
                        onCheckChange = { viewModel.onReminderCheckChange(reminderItem) },
                        onActionClick = { action -> viewModel.onReminderActionClick(openScreen, reminderItem, action) }
                    )
                }
            }
        }
    }

    LaunchedEffect(viewModel) { viewModel.loadReminderOptions() }
}
