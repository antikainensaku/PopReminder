package com.antisoftware.popreminder.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antisoftware.popreminder.common.composable.BasicButton
import com.antisoftware.popreminder.common.composable.BasicImage
import com.antisoftware.popreminder.common.composable.DividerText
import com.antisoftware.popreminder.common.extension.basicButton
import com.antisoftware.popreminder.common.extension.basicImage
import com.antisoftware.popreminder.common.extension.spacer
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.R.drawable as AppIcon

@ExperimentalMaterialApi
@Composable
fun WelcomeScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicImage(imageId = AppIcon.pop_icon_final, modifier = Modifier.basicImage())
        BasicButton(
            AppText.create_account,
            Modifier.basicButton(),
            bgColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.primary
        ) {
            viewModel.onCreateAccountClick(openScreen)
        }
        DividerText(text = "Already have an account?", modifier = Modifier.padding(16.dp))
        BasicButton(
            AppText.log_in,
            Modifier.basicButton()
        ) {
            viewModel.onSignInClick(openScreen)
        }
    }
}