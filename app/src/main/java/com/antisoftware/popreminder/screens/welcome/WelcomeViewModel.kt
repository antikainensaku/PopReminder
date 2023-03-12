package com.antisoftware.popreminder.screens.welcome

import com.antisoftware.popreminder.LOGIN_SCREEN
import com.antisoftware.popreminder.SIGN_UP_SCREEN
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    logService: LogService
) : PopReminderViewModel(logService) {
    fun onSignInClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)
    fun onCreateAccountClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)
}