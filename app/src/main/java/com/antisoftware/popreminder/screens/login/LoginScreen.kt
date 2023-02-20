package com.antisoftware.popreminder.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.antisoftware.popreminder.common.composable.*
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.common.composable.*
import com.antisoftware.popreminder.common.extension.basicButton
import com.antisoftware.popreminder.common.extension.fieldModifier
import com.antisoftware.popreminder.common.extension.textButton
import com.antisoftware.popreminder.screens.login.LoginViewModel

@Composable
fun LoginScreen(
    restartApp: (String) -> Unit,
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    BasicToolbar(AppText.login_details) { popUpScreen() }

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

        BasicButton(AppText.sign_in, Modifier.basicButton()) { viewModel.onSignInClick(restartApp) }

        BasicTextButton(AppText.forgot_pw, Modifier.textButton()) {
            viewModel.onForgotPasswordClick()
        }
    }
}
