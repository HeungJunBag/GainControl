package com.heungjun.gaincontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.screens.MainScreen
import com.heungjun.gaincontrol.pages.LoginPage
import com.heungjun.gaincontrol.pages.SignupPage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)

    // 동적으로 startDestination 설정
    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "home" else "login" // 로그인 상태에 따라 변경
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
        composable("home") {
            MainScreen(authViewModel = authViewModel) // 로그인된 사용자 화면
        }
    }
}