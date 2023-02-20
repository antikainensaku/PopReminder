package com.example.makeitso.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.SPLASH_SCREEN
import com.antisoftware.popreminder.REMINDER_SCREEN
import com.antisoftware.popreminder.WELCOME_SCREEN
import com.antisoftware.popreminder.data.firebase.ConfigurationService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : PopReminderViewModel(logService) {
    val showError = mutableStateOf(false)

    init {
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser) openAndPopUp(REMINDER_SCREEN, SPLASH_SCREEN)
        else openAndPopUp(WELCOME_SCREEN, SPLASH_SCREEN)
    }
    /*  *** Anonymous account disabled atm ***
    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
      launchCatching(snackbar = false) {
        try {
          accountService.createAnonymousAccount()
        } catch (ex: FirebaseAuthException) {
          showError.value = true
          throw ex
        }
        openAndPopUp(REMINDER_SCREEN, SPLASH_SCREEN)
      }
    }*/
}
