package com.hantash.notemark.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hantash.notemark.ui.screen.LandingScreen
import com.hantash.notemark.ui.screen.LoginScreen
import com.hantash.notemark.ui.screen.SignUpScreen

@Composable
fun ScreenNavigation() {
    val navController = rememberNavController()
    val defaultScreen = EnumScreen.LANDING.name

    NavHost(navController = navController, startDestination = defaultScreen) {

        composable(defaultScreen) {
            LandingScreen(navController)
        }

        composable(route = EnumScreen.LOGIN.name) {
            LoginScreen(navController)
        }

        composable(route = EnumScreen.SIGN_UP.name) {
            SignUpScreen(navController)
        }
    }
}