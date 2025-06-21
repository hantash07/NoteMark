package com.hantash.notemark.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hantash.notemark.R
import com.hantash.notemark.ui.component.AppButton
import com.hantash.notemark.ui.component.AppOutlineButton
import com.hantash.notemark.ui.component.AppSpacer
import com.hantash.notemark.ui.component.EnumSpacer
import com.hantash.notemark.ui.component.TopHeading
import com.hantash.notemark.ui.navigation.EnumScreen

@Preview(showBackground = true)
@Composable
fun LandingScreen(navController: NavController? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.bg_landing),
            contentDescription = "Background Landing Screen",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(24.dp)
        ) {

            TopHeading(title = "Your Own Collection of Notes", message = "Capture your thoughts and ideas.")

            AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
            AppButton(text = "Get Started", onClick = {
                navigateTo(navController = navController, screenName = EnumScreen.SIGN_UP.name)
            })

            AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
            AppOutlineButton(text = "Login", onClick = {
                navigateTo(navController = navController, screenName = EnumScreen.LOGIN.name)
            })

        }
    }
}

private fun navigateTo(navController: NavController?, screenName: String) {
    navController?.navigate(screenName) {
        popUpTo(EnumScreen.LANDING.name) {inclusive = true}
    }
}