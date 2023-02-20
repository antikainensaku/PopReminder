package com.antisoftware.popreminder.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.antisoftware.popreminder.LOGIN_SCREEN
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.PROFILE_SCREEN
import com.antisoftware.popreminder.SIGN_UP_SCREEN
import com.antisoftware.popreminder.common.extension.isValidEmail
import com.antisoftware.popreminder.common.extension.isValidPassword
import com.antisoftware.popreminder.common.extension.passwordMatches
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : PopReminderViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val name
        get() = uiState.value.name
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

        launchCatching {
            accountService.createAccount(email, password)
            accountService.updateDisplayName(name)
            openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
        }
    }

    fun onBackClick(popUpScreen: () -> Unit) {
        popUpScreen()
    }
}
