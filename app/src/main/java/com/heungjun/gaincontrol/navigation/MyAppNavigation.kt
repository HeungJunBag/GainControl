package com.heungjun.gaincontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heungjun.gaincontrol.AddictionForm
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.screens.MainScreen
import com.heungjun.gaincontrol.pages.LoginPage
import com.heungjun.gaincontrol.pages.SignupPage
import com.heungjun.gaincontrol.screens.HomePage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "addiction_form" else "login"
    ) {
        composable("login") {
            LoginPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("addiction_form") {
            AddictionForm(navController=navController)
        }
        composable("home") {
            MainScreen(
                authViewModel = authViewModel
            )
        }
    }
}
