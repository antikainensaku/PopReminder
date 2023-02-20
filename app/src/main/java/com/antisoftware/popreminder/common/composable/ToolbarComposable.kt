package com.antisoftware.popreminder.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

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
