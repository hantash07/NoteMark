package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.ui.component.AppButton
import com.hantash.notemark.ui.component.AppSpacer
import com.hantash.notemark.ui.component.AppTextButton
import com.hantash.notemark.ui.component.EnumInputType
import com.hantash.notemark.ui.component.EnumSpacer
import com.hantash.notemark.ui.component.InputField
import com.hantash.notemark.ui.component.TopHeading
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.SurfaceLowest
import com.hantash.notemark.utils.isValidEmail
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onNavigateTo: (EnumScreen) -> Unit) {
    val focusManager = LocalFocusManager.current
    val snackBarHost = remember { SnackbarHostState() }

    val viewModel: AuthViewModel = hiltViewModel()
    val uiState = viewModel.uiLoginState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigateTo.invoke(event.enumScreen)
                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHost.showSnackbar(message = event.message)
                }

                else -> {}
            }
        }
    }

    LoginScaffold(
        snackBarHost = {
            SnackbarHost(snackBarHost)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .background(color = Primary)
                    .padding(paddingValues)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                when (localScreenOrientation.current) {
                    DevicePosture.MOBILE_PORTRAIT, DevicePosture.TABLET_PORTRAIT -> LoginPortrait(
                        uiState = uiState.value,
                        onNavigateTo = {

                        },
                        onLogin = { email, password ->
                            viewModel.login(email, password)
                        }
                    )

                    DevicePosture.MOBILE_LANDSCAPE, DevicePosture.TABLET_LANDSCAPE -> LoginLandscape(
                        uiState = uiState.value,
                        onNavigateTo = {

                        },
                        onLogin = { email, password ->
                            viewModel.login(email, password)
                        }
                    )

                    else -> LoginPortrait(
                        uiState = uiState.value,
                        onNavigateTo = {

                        },
                        onLogin = { email, password ->
                            viewModel.login(email, password)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun LoginScaffold(
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        snackbarHost = snackBarHost,
        content = content
    )
}

@Composable
private fun LoginPortrait(
    uiState: UiState<Unit> = UiState.Idle,
    onNavigateTo: (EnumScreen) -> Unit = {},
    onLogin: (String, String) -> Unit = {email, password, -> }) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = SurfaceLowest,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(16.dp),
    ) {

        AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
        TopHeading(title = "Log In", message = "Capture your thoughts and ideas.")

        AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
        LoginContent(uiState, onNavigateTo, onLogin)
    }
}

@Composable
private fun LoginLandscape(
    uiState: UiState<Unit> = UiState.Idle,
    onNavigateTo: (EnumScreen) -> Unit = {},
    onLogin: (String, String) -> Unit = { email, password -> }) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = SurfaceLowest,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(start = 60.dp, end = 24.dp, top = 24.dp, bottom = 24.dp)
    ) {
        TopHeading(
            modifier = Modifier.weight(1f),
            title = "Log In",
            message = "Capture your thoughts and ideas."
        )

        LoginContent(uiState, onNavigateTo, onLogin)
    }
}

@Composable
private fun LoginContent(
    uiState: UiState<Unit> = UiState.Idle,
    onNavigateTo: (EnumScreen) -> Unit = {},
    onLogin: (String, String) -> Unit = { email, password -> }
) {
    val focusManager = LocalFocusManager.current

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }

    val isEmailValid = remember {
        derivedStateOf { email.value.isValidEmail() }
    }
    val isPassword = remember {
        derivedStateOf { password.value.isNotEmpty() }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputField(
            type = EnumInputType.EMAIL,
            keyboardType = KeyboardType.Email,
            name = "Email",
            placeholder = "john.doe@example.com",
            value = email.value,
            onValueChange = { value ->
                email.value = value
            },
        )

        AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
        InputField(
            type = EnumInputType.PASSWORD,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            name = "Password",
            placeholder = "Password",
            value = password.value,
            onValueChange = { value ->
                password.value = value
            },
            isPasswordVisible = passwordVisibility.value,
            onPasswordVisibility = {
                passwordVisibility.value = !passwordVisibility.value
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
        AppButton(
            text = "Log in",
            isEnable = isEmailValid.value && isPassword.value,
            isLoading = uiState is UiState.Loading,
            onClick = {
                focusManager.clearFocus()
                onLogin(email.value, password.value)
//                viewModel.login(email.value, password.value)
            }
        )

        AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
        AppTextButton(
            text = "Don't have an account?",
            onClick = {
                onNavigateTo.invoke(EnumScreen.SIGN_UP)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginPortrait(navController: NavController? = null) {
    LoginPortrait()
}

@Preview(showBackground = true, widthDp = 740, heightDp = 360)
@Composable
fun PreviewLoginLandscape(navController: NavController? = null) {
    LoginLandscape()
}


