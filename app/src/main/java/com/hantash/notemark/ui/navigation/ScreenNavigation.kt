package com.hantash.notemark.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hantash.notemark.ui.screen.LandingScreen
import com.hantash.notemark.ui.screen.LoginScreen
import com.hantash.notemark.ui.screen.NoteAddEditScreen
import com.hantash.notemark.ui.screen.NoteDetailScreen
import com.hantash.notemark.ui.screen.NoteListScreen
import com.hantash.notemark.ui.screen.SettingsScreen
import com.hantash.notemark.ui.screen.SignUpScreen
import com.hantash.notemark.viewmodel.AuthViewModel

@Composable
fun ScreenNavigation() {
    val viewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn = viewModel.isUserLoggedInState.collectAsState()

    val navController = rememberNavController()
    val startingScreen = if (!isLoggedIn.value) EnumScreen.LANDING.name else EnumScreen.NOTE_LIST.name

    NavHost(navController = navController, startDestination = startingScreen) {

        composable(startingScreen) {
            LandingScreen(onNavigateTo = { enumScreen ->
                navController.navigate(enumScreen.name) {
                    popUpTo(startingScreen) { inclusive = true } //Removing Landing Screen from Screen's Stack
                }
            })
        }

        composable(route = EnumScreen.LOGIN.name) {
            LoginScreen(onNavigateTo =  { enumScreen ->
                if (enumScreen == EnumScreen.SIGN_UP) {
                    navController.navigate(EnumScreen.SIGN_UP.name)
                }

                if (enumScreen == EnumScreen.NOTE_LIST) {
                    navController.navigate(enumScreen.name) {
                        popUpTo(EnumScreen.LOGIN.name) { inclusive = true }
                    }
                }
            })
        }

        composable(route = EnumScreen.SIGN_UP.name) {
            SignUpScreen(onNavigateTo = { enumScreen ->
                if (enumScreen == EnumScreen.LOGIN) {
                    val isNavigated = navController.popBackStack()
                    if (!isNavigated) {
                        navController.navigate(enumScreen.name) {
                            popUpTo(EnumScreen.SIGN_UP.name) { inclusive = true }
                        }
                    }
                }

                if (enumScreen == EnumScreen.NOTE_LIST) {
                    navController.navigate(enumScreen.name) {
                        popUpTo(EnumScreen.SIGN_UP.name) { inclusive = true }
                    }
                }
            })
        }

        composable(route = EnumScreen.NOTE_LIST.name) {
            NoteListScreen(navController)
        }

        composable(route = EnumScreen.NOTE_ADD_EDIT.name) {
            NoteAddEditScreen(navController)
        }

        composable(route = EnumScreen.NOTE_DETAIL.name) {
            NoteDetailScreen(navController)
        }

        composable(route = EnumScreen.SETTINGS.name) {
            SettingsScreen(navController)
        }
    }
}