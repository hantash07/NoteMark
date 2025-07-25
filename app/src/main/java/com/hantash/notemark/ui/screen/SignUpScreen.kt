package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
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
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.model.User
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
import com.hantash.notemark.utils.isValidPassword
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(onNavigateTo: (EnumScreen) -> Unit) {
    val focusManager = LocalFocusManager.current
    val snackBarHost = remember { SnackbarHostState() }

    val viewModel: AuthViewModel = hiltViewModel()
    val uiState = viewModel.uiSignUpState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Authenticate -> {
                    viewModel.login(event.email, event.password)
                }

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

    SignUpScaffold(
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
                    DevicePosture.MOBILE_PORTRAIT, DevicePosture.TABLET_PORTRAIT -> SignUpPortrait(
                        uiState = uiState.value,
                        onNavigateTo = onNavigateTo,
                        onRegister = { user ->
                            viewModel.register(
                                username = user.username,
                                email = user.email,
                                password = user.password
                            )
                        }
                    )

                    DevicePosture.MOBILE_LANDSCAPE, DevicePosture.TABLET_LANDSCAPE -> SignUpLandscape(
                        uiState = uiState.value,
                        onNavigateTo = onNavigateTo,
                        onRegister = { user ->
                            viewModel.register(
                                username = user.username,
                                email = user.email,
                                password = user.password
                            )
                        }
                    )

                    else -> SignUpPortrait(
                        uiState = uiState.value,
                        onNavigateTo = onNavigateTo,
                        onRegister = { user ->
                            viewModel.register(
                                username = user.username,
                                email = user.email,
                                password = user.password
                            )
                        }
                    )
                }
            }
        }
    )

}

@Composable
private fun SignUpScaffold(
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        snackbarHost = snackBarHost,
        content = content
    )
}

@Composable
private fun SignUpPortrait(
    uiState: UiState<Unit> = UiState.Idle,
    onNavigateTo: (EnumScreen) -> Unit = {},
    onRegister: (User) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp)
            .background(
                color = SurfaceLowest,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(16.dp),
    ) {
        AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
        TopHeading(title = "Create account", message = "Capture your thoughts and ideas.")

        AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
        SignUpContent(uiState = uiState, onNavigateTo = onNavigateTo, onRegister = onRegister)
    }
}

@Composable
private fun SignUpLandscape(
    uiState: UiState<Unit> = UiState.Idle,
    onNavigateTo: (EnumScreen) -> Unit = {},
    onRegister: (User) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp)
            .background(
                color = SurfaceLowest,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(start = 60.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
    ) {
        TopHeading(
            modifier = Modifier.weight(1f),
            title = "Create account",
            message = "Capture your thoughts and ideas."
        )

        SignUpContent(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            onNavigateTo = onNavigateTo,
            onRegister = onRegister
        )
    }
}

@Composable
private fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: UiState<Unit> = UiState.Idle,
    onNavigateTo: (EnumScreen) -> Unit = {},
    onRegister: (User) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    val username = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordRepeat = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordRepeatVisibility = rememberSaveable { mutableStateOf(false) }

    val isUsernameValid = remember {
        derivedStateOf {
            username.value.length in 3..20
        }
    }
    val isEmailValid = remember {
        derivedStateOf {
            email.value.isValidEmail()
        }
    }
    val isPasswordValid = remember {
        derivedStateOf {
            password.value.isValidPassword()
        }
    }
    val isPasswordRepeatValid = remember {
        derivedStateOf {
            password.value == passwordRepeat.value
        }
    }

    val isSignUpValid = remember {
        derivedStateOf {
            isUsernameValid.value && isEmailValid.value && isPasswordValid.value && isPasswordRepeatValid.value
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputField(
            type = EnumInputType.USERNAME,
            name = "Username",
            placeholder = "john.doe",
            supportingText = "Use between 3 and 20 characters for your username.",
            errorText = if (username.value.length < 3) "Username must be at least 3 characters." else "Username can’t be longer than 20 characters.",
            isValid = isUsernameValid.value,
            value = username.value,
            onValueChange = { value -> username.value = value },
        )

        AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
        InputField(
            type = EnumInputType.EMAIL,
            keyboardType = KeyboardType.Email,
            name = "Email",
            placeholder = "john.doe@example.com",
            errorText = "Invalid email provided",
            isValid = isEmailValid.value,
            value = email.value,
            onValueChange = { value -> email.value = value },
        )

        AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
        InputField(
            type = EnumInputType.PASSWORD,
            keyboardType = KeyboardType.Password,
            name = "Password",
            placeholder = "Password",
            supportingText = "Use 8+ characters with a number or symbol for better security.",
            errorText = "Password must be at least 8 characters and include a number or symbol.",
            isValid = isPasswordValid.value,
            value = password.value,
            onValueChange = { value -> password.value = value },
            isPasswordVisible = passwordVisibility.value,
            onPasswordVisibility = {
                passwordVisibility.value = !passwordVisibility.value
            }
        )

        AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
        InputField(
            type = EnumInputType.PASSWORD,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            name = "Repeat Password",
            placeholder = "Password",
            errorText = "Passwords do not match",
            isValid = isPasswordRepeatValid.value,
            value = passwordRepeat.value,
            onValueChange = { value ->
                passwordRepeat.value = value
            },
            isPasswordVisible = passwordRepeatVisibility.value,
            onPasswordVisibility = {
                passwordRepeatVisibility.value = !passwordRepeatVisibility.value
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
        AppButton(
            text = "Create account",
            isEnable = isSignUpValid.value,
            isLoading = (uiState is UiState.Loading),
            onClick = {
                focusManager.clearFocus()
                onRegister(
                    User(
                        username = username.value,
                        email = email.value,
                        password = password.value
                    )
                )
            }
        )

        AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
        AppTextButton(
            text = "Already have an account?",
            isEnable = uiState is UiState.Idle,
            onClick = {
                onNavigateTo.invoke(EnumScreen.LOGIN)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSignUpPortrait() {
    SignUpPortrait()
}

@Preview(showBackground = true, widthDp = 740, heightDp = 360)
@Composable
private fun PreviewSignUpLandscape() {
    SignUpLandscape()
}
