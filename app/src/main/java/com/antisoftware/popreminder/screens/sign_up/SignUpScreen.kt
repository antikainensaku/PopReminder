package com.antisoftware.popreminder.screens.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.antisoftware.popreminder.common.composable.*
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.common.composable.*
import com.antisoftware.popreminder.common.extension.basicButton
import com.antisoftware.popreminder.common.extension.fieldModifier
import com.antisoftware.popreminder.screens.sign_up.SignUpViewModel

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    BasicToolbar(AppText.create_account) { viewModel.onBackClick(popUpScreen) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameField(uiState.name, viewModel::onNameChange, Modifier.fieldModifier())
        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())
        RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, Modifier.fieldModifier())

        BasicButton(AppText.create_account, Modifier.basicButton()) {
            viewModel.onSignUpClick(openAndPopUp)
        }
    }
}
