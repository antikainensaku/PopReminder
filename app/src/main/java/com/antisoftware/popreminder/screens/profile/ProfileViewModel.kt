package com.antisoftware.popreminder.screens.profile

import com.antisoftware.popreminder.SPLASH_SCREEN
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class ProfileViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : PopReminderViewModel(logService) {

    fun getEmail() : String {
        return accountService.getUserEmail()
    }

    fun getName() : String {
        return accountService.getName()
    }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            storageService.deleteAllForUser(accountService.currentUserId)
            accountService.deleteAccount()
            restartApp(SPLASH_SCREEN)
        }
    }
}
