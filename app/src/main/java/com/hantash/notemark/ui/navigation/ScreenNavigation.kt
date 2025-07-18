package com.hantash.notemark.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hantash.notemark.ui.screen.LandingScreen
import com.hantash.notemark.ui.screen.LoginScreen
import com.hantash.notemark.ui.screen.NoteAddEditScreen
import com.hantash.notemark.ui.screen.NoteDetailScreen
import com.hantash.notemark.ui.screen.NoteListScreen
import com.hantash.notemark.ui.screen.SettingsScreen
import com.hantash.notemark.ui.screen.SignUpScreen
import com.hantash.notemark.utils.debug
import com.hantash.notemark.viewmodel.AuthViewModel
import kotlinx.coroutines.cancel

@Composable
fun ScreenNavigation() {
    val viewModel: AuthViewModel = hiltViewModel()

    val navController = rememberNavController()
    var startingScreen by rememberSaveable { mutableStateOf<String?>(null) }
    val argNoteId = "noteId"

    LaunchedEffect(Unit) {
        viewModel.isUserLoggedInState.collect { isLoggedIn ->
            isLoggedIn?.let { isLogin ->
                startingScreen = if (!isLogin) EnumScreen.LANDING.name else EnumScreen.NOTE_LIST.name
                cancel()
            }
        }
    }

    if (startingScreen.isNullOrEmpty()) return

    debug("ScreenNavigation")

    NavHost(navController = navController, startDestination = startingScreen!!) {

        composable(startingScreen!!) {
            LandingScreen(onNavigateTo = { enumScreen ->
                navController.navigate(enumScreen.name) {
                    popUpTo(startingScreen!!) {
                        inclusive = true
                    } //Removing Landing Screen from Screen's Stack
                }
            })
        }

        composable(route = EnumScreen.LOGIN.name) {
            LoginScreen(onNavigateTo = { enumScreen ->
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
            NoteListScreen(
                onNavigateTo = { enumScreen ->
                    navController.navigate(enumScreen.name)
                },
                onNavWithArguments = { enumScreen, argValue ->
                    navController.navigate(enumScreen.name + "/$argValue")
                }
            )
        }

        composable(
            route = EnumScreen.NOTE_ADD_EDIT.name + "/{$argNoteId}",
            arguments = listOf(navArgument(argNoteId) { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(argNoteId) ?: ""
            NoteAddEditScreen(
                noteId = noteId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = EnumScreen.NOTE_DETAIL.name + "/{$argNoteId}",
            arguments = listOf(navArgument(argNoteId) { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(argNoteId) ?: ""
            NoteDetailScreen(
                noteId = noteId,
                onNavWithArguments = { enumScreen, argValue ->
                    navController.navigate(enumScreen.name + "/$argValue")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = EnumScreen.SETTINGS.name) {
            SettingsScreen(
                onNavigateTo = { enumScreen ->
                    if (enumScreen == EnumScreen.LOGIN) {
                        navController.navigate(enumScreen.name) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}