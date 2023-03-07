package com.antisoftware.popreminder.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun BasicToolbar(@StringRes title: Int, action: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        navigationIcon = { BasicIconButton { action() } },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun BasicToolbar(@StringRes title: Int) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun ActionToolbar(
    @StringRes title: Int,
    @DrawableRes endActionIcon: Int,
    modifier: Modifier,
    endAction: () -> Unit,
    backAction: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            BasicIconButton {
                backAction()
            }},
        actions = {
            Box(modifier) {
                IconButton(onClick = endAction) {
                    Icon(painter = painterResource(endActionIcon), contentDescription = "Action")
                }
            }
        }
    )
}

@Composable
fun ActionToolbar(
    @StringRes title: Int,
    @DrawableRes endActionIcon: Int,
    modifier: Modifier,
    endAction: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            Box(modifier) {
                IconButton(onClick = endAction) {
                    Icon(painter = painterResource(endActionIcon), contentDescription = "Action")
                }
            }
        }
    )
}

@Composable
fun ActionToolbar(
    @StringRes title: Int,
    @DrawableRes endActionIcon: Int,
    modifier: Modifier,
    checked: Boolean,
    onCheckChange: () -> Unit,
    endAction: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(text = "Show all")
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckChange() },
                    modifier = Modifier.padding(8.dp, 0.dp)
                )
                Box(modifier) {
                    IconButton(onClick = endAction) {
                        Icon(painter = painterResource(endActionIcon), contentDescription = "Action")
                    }
                }
            }
        }
    )
}
