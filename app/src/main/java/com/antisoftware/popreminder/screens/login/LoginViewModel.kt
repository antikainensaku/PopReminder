package com.antisoftware.popreminder.screens.login

import androidx.compose.runtime.mutableStateOf
import com.antisoftware.popreminder.LOGIN_SCREEN
import com.antisoftware.popreminder.PROFILE_SCREEN
import com.antisoftware.popreminder.REMINDER_SCREEN
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.common.extension.isValidEmail
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : PopReminderViewModel(logService) {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(restartApp: (String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            restartApp(REMINDER_SCREEN)
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(AppText.recovery_email_sent)
        }
    }
}
