package com.antisoftware.popreminder.screens.welcome

import com.antisoftware.popreminder.LOGIN_SCREEN
import com.antisoftware.popreminder.PROFILE_SCREEN
import com.antisoftware.popreminder.SIGN_UP_SCREEN
import com.antisoftware.popreminder.WELCOME_SCREEN
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.ConfigurationService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import com.antisoftware.popreminder.screens.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    logService: LogService
) : PopReminderViewModel(logService) {
    fun onSignInClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)
    fun onCreateAccountClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)
}