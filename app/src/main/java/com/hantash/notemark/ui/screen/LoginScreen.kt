package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.SurfaceLowest

@Preview(showBackground = true)
@Composable
fun LoginScreen(navController: NavController? = null) {
    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Primary)
                    .padding(innerPadding)
                    .padding(top = 4.dp)
                    .background(color = SurfaceLowest, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
                TopHeading(title = "Log In", message = "Capture your thoughts and ideas.")

                AppSpacer(dp = 32.dp, EnumSpacer.HEIGHT)
                InputField(
                    type = EnumInputType.EMAIL,
                    name = "Email",
                    placeholder = "john.doe@example.com",
                    value = "",
                    onValueChange = { value ->

                    })

                AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
                InputField(
                    type = EnumInputType.PASSWORD,
                    keyboardType = KeyboardType.Password,
                    name = "Password",
                    placeholder = "Password",
                    value = "",
                    onValueChange = {

                    },
                    onPasswordVisibility = {

                    }
                )

                AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
                AppButton(text = "Log in")

                AppTextButton(text = "Don't have an account?")
            }
        }
    )
}






