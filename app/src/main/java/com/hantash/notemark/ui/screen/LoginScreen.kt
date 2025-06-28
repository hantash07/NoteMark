package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hantash.notemark.DevicePosture
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
import com.hantash.notemark.utils.debug
import com.hantash.notemark.utils.isValidEmail
import com.hantash.notemark.utils.localScreenOrientation

@Composable
fun LoginScreen(navController: NavController? = null) {
    val focusManager = LocalFocusManager.current

    Scaffold(
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
                        navController
                    )

                    DevicePosture.MOBILE_LANDSCAPE, DevicePosture.TABLET_LANDSCAPE -> LoginLandscape(
                        navController
                    )

                    else -> LoginPortrait(navController)
                }
            }
        }
    )
}

@Composable
private fun LoginPortrait(navController: NavController? = null) {
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
        LoginContent(navController)
    }
}

@Composable
private fun LoginLandscape(navController: NavController? = null) {
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

        LoginContent(modifier = Modifier.weight(1f), navController = navController)
    }
}

@Composable
private fun LoginContent(navController: NavController? = null, modifier: Modifier = Modifier) {
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
        modifier = modifier,
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
            onClick = {
                focusManager.clearFocus()
            }
        )

        AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
        AppTextButton(
            text = "Don't have an account?",
            onClick = {
                navController?.navigate(EnumScreen.SIGN_UP.name)
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


