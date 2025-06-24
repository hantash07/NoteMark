package com.hantash.notemark

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hantash.notemark.ui.navigation.ScreenNavigation
import com.hantash.notemark.ui.theme.NoteMarkTheme
import com.hantash.notemark.utils.localScreenOrientation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()

        enableEdgeToEdge()
        setContent {
            NoteMarkTheme {
                ProvideOrientation {
                    ScreenNavigation()
                }
            }
        }
    }

    private fun initSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            false
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun ProvideOrientation(content: @Composable () -> Unit) {
        val configuration = LocalConfiguration.current

        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val windowSizeClass = calculateWindowSizeClass(this)

        val orientation = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                if (isLandscape) DevicePosture.MOBILE_LANDSCAPE else DevicePosture.MOBILE_PORTRAIT
            }
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                if (isLandscape) DevicePosture.TABLET_LANDSCAPE else DevicePosture.TABLET_PORTRAIT
            }
            else -> DevicePosture.MOBILE_PORTRAIT
        }

        CompositionLocalProvider(localScreenOrientation provides orientation) {
            content()
        }
    }
}

enum class DevicePosture {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
}
