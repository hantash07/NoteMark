package com.hantash.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hantash.notemark.ui.navigation.ScreenNavigation
import com.hantash.notemark.ui.theme.NoteMarkTheme
import com.hantash.notemark.utils.debug
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: AuthViewModel by viewModels()

        //Init Splash Screen
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            //Checking the login state. If its null it means no value is assign and continue showing the splash screen.
            //Once the true or false value assign the splash screen will stop showing.
            viewModel.isUserLoggedInState.value == null
        }

        enableEdgeToEdge()
        setContent {
            NoteMarkTheme {
                ProvideOrientation {
                    debug("MainActivity")
                    ScreenNavigation()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun ProvideOrientation(content: @Composable () -> Unit) {
//        val configuration = LocalConfiguration.current
//        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val windowSizeClass = calculateWindowSizeClass(this)
        val widthClass = windowSizeClass.widthSizeClass
        val heightClass = windowSizeClass.heightSizeClass

        debug("widthClass: $widthClass === heightClass: $heightClass")
        val orientation = when {
            widthClass == WindowWidthSizeClass.Compact && heightClass == WindowHeightSizeClass.Medium
                -> DevicePosture.MOBILE_PORTRAIT
            widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Compact
                -> DevicePosture.MOBILE_LANDSCAPE
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded
                -> DevicePosture.TABLET_PORTRAIT
            widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Medium
                -> DevicePosture.TABLET_LANDSCAPE
            else -> DevicePosture.DESKTOP
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
    DESKTOP;
}
