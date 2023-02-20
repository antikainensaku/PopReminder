package com.antisoftware.popreminder.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.antisoftware.popreminder.data.firebase.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class PopReminderViewModel(private val logService: LogService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}
