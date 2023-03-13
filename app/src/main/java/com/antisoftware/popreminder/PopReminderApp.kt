package com.antisoftware.popreminder

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.antisoftware.popreminder.theme.PopReminderTheme
import com.antisoftware.popreminder.common.snackbar.SnackbarManager
import com.antisoftware.popreminder.screens.edit.EditReminderScreen
import com.antisoftware.popreminder.screens.login.LoginScreen
import com.antisoftware.popreminder.screens.maps.MapScreen
import com.antisoftware.popreminder.screens.profile.ProfileScreen
import com.antisoftware.popreminder.screens.reminders.RemindersScreen
import com.antisoftware.popreminder.screens.sign_up.SignUpScreen
import com.antisoftware.popreminder.screens.splash.SplashScreen
import com.antisoftware.popreminder.screens.welcome.WelcomeScreen
import kotlinx.coroutines.CoroutineScope


@Composable
@ExperimentalMaterialApi
fun PopReminderApp() {
    PopReminderTheme() {
        Surface(color = MaterialTheme.colors.background) {
            val appState = rememberAppState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    popReminderGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        PopReminderAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@ExperimentalMaterialApi
fun NavGraphBuilder.popReminderGraph(appState: PopReminderAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(WELCOME_SCREEN) {
        WelcomeScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(PROFILE_SCREEN) {
        ProfileScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) },
            popUpScreen = { appState.popUp() }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            popUpScreen = { appState.popUp() }
        )
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            popUpScreen = { appState.popUp() }
        )
    }

    composable(REMINDER_SCREEN) {
        RemindersScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }
    
    composable(MAP_SCREEN) {
        MapScreen(popUpScreen = { appState.popUp() })
    }

    composable(
        route = "$EDIT_REMINDER_SCREEN$REMINDER_ID_ARG",
        arguments = listOf(navArgument(REMINDER_ID) { defaultValue = REMINDER_DEFAULT_ID })
    ) {
        EditReminderScreen(
            popUpScreen = { appState.popUp() },
            openScreen = { route -> appState.navigate(route) },
            reminderId = it.arguments?.getString(REMINDER_ID) ?: REMINDER_DEFAULT_ID
        )
    }
}