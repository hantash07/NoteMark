package com.hantash.notemark.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hantash.notemark.ui.component.AppButton
import com.hantash.notemark.ui.component.AppSpacer
import com.hantash.notemark.ui.component.AppTextButton
import com.hantash.notemark.ui.component.EnumSpacer
import com.hantash.notemark.ui.component.InputField
import com.hantash.notemark.ui.component.TopHeading

@Preview(showBackground = true)
@Composable
fun LoginScreen(navController: NavController? = null) {
    Scaffold (
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
                TopHeading(title = "Log In", message = "Capture your thoughts and ideas.")

                AppSpacer(dp = 32.dp, EnumSpacer.HEIGHT)
                InputField(name = "Email", value = "", onValueChange = {})

                AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
                InputField(name = "Password", value = "", onValueChange = {})

                AppSpacer(dp = 16.dp, EnumSpacer.HEIGHT)
                AppButton(text = "Log in")

                AppTextButton(text = "Don't have an account?")
            }
        }
    )
}






