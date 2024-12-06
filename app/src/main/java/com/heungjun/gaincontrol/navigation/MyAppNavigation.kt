package com.heungjun.gaincontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.heungjun.gaincontrol.viewmodel.UserViewModel

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)
    val isNewUser by userViewModel.isNewUser.observeAsState(false)

    NavHost(
        navController = navController,
        startDestination = "login"
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
            AddictionForm(navController = navController)
        }
        composable("home") {
            if (isNewUser) {
                LaunchedEffect(Unit) {
                    navController.navigate("addiction_form")
                }
            } else {
                HomePage(
                    navController = navController,
                    authViewModel = authViewModel,
                    userViewModel = userViewModel
                )
            }
        }
    }
}
