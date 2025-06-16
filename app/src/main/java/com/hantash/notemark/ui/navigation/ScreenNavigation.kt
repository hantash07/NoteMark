package com.hantash.notemark.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hantash.notemark.ui.screen.LoginScreen

@Composable
fun ScreenNavigation() {
    val navController = rememberNavController()
    val defaultScreen = EnumScreen.LOGIN.name

    NavHost(navController = navController, startDestination = defaultScreen) {
        composable(defaultScreen) {
            LoginScreen(navController)
        }
    }
}