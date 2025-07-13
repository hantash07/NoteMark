package com.hantash.notemark.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.R
import com.hantash.notemark.ui.component.AppButton
import com.hantash.notemark.ui.component.AppOutlineButton
import com.hantash.notemark.ui.component.AppSpacer
import com.hantash.notemark.ui.component.EnumSpacer
import com.hantash.notemark.ui.component.TopHeading
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.theme.LandingBg
import com.hantash.notemark.utils.localScreenOrientation

@Composable
fun LandingScreen(onNavigateTo: (EnumScreen) -> Unit) {
    when (localScreenOrientation.current) {
        DevicePosture.MOBILE_PORTRAIT, DevicePosture.TABLET_PORTRAIT -> LandingPortrait(onNavigateTo)
        DevicePosture.MOBILE_LANDSCAPE, DevicePosture.TABLET_LANDSCAPE -> LandingLandscape(onNavigateTo)
        else -> LandingPortrait(onNavigateTo)
    }
}

@Composable
private fun LandingContent(onNavigateTo: (EnumScreen) -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        TopHeading(
            title = "Your Own Collection of Notes",
            message = "Capture your thoughts and ideas."
        )

        AppSpacer(dp = 24.dp, EnumSpacer.HEIGHT)
        AppButton(text = "Get Started", onClick = {
            onNavigateTo.invoke(EnumScreen.SIGN_UP)
        })

        AppSpacer(dp = 8.dp, EnumSpacer.HEIGHT)
        AppOutlineButton(text = "Login", onClick = {
            onNavigateTo.invoke(EnumScreen.LOGIN)
        })

    }
}

@Composable
private fun LandingPortrait(onNavigateTo: (EnumScreen) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LandingBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.bg_landing_portrait),
            contentDescription = "Background Landing Screen",
            contentScale = ContentScale.Crop
        )

        LandingContent(
            onNavigateTo,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(24.dp)
        )
    }
}

@Composable
private fun LandingLandscape(onNavigateTo: (EnumScreen) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LandingBg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            painter = painterResource(id = R.drawable.bg_landing_landscape),
            contentDescription = "Background Landing Screen",
        )

        LandingContent(
            onNavigateTo,
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .padding(40.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLandingPortrait() {
    LandingPortrait()
}

@Preview(showBackground = true, widthDp = 740, heightDp = 360)
@Composable
private fun PreviewLandingLandscape() {
    LandingLandscape()
}