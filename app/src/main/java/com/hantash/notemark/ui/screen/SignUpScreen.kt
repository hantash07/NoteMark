package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

@Preview(showBackground = true)
@Composable
fun SignUpScreen(navController: NavController? = null) {
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

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .background(color = Primary)
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(top = 4.dp)
                    .background(
                        color = SurfaceLowest,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
                TopHeading(title = "Create account", message = "Capture your thoughts and ideas.")

                AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
                InputField(
                    type = EnumInputType.USERNAME,
                    name = "Username",
                    placeholder = "john.doe",
                    supportingText = "Use between 3 and 20 characters for your username.",
                    errorText = if (username.value.length < 3) "Username must be at least 3 characters." else "Username can’t be longer than 20 characters.",
                    isValid = isUsernameValid.value,
                    value = username.value,
                    onValueChange = { value -> username.value = value},
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
                    onValueChange = { value -> email.value = value},
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
                    onValueChange = { value -> password.value = value},
                    isPasswordVisible = passwordVisibility.value,
                    onPasswordVisibility = {
                        passwordVisibility.value = !passwordVisibility.value
                    }
                )

                AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
                InputField(
                    type = EnumInputType.PASSWORD,
                    keyboardType = KeyboardType.Password,
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
                    }
                )

                AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
                AppButton(text = "Create account", isEnable = isSignUpValid.value)

                AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
                AppTextButton(text = "Already have an account?", onClick = {
                    if (navController?.popBackStack() == false) {
                        navController.navigate(EnumScreen.LOGIN.name) {
                            popUpTo(EnumScreen.SIGN_UP.name) {inclusive = true}
                        }
                    }
                })
            }
        }
    )

}
