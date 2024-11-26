package com.heungjun.gaincontrol.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.screens.HomePage
import com.heungjun.gaincontrol.screens.SettingsScreen
import com.heungjun.gaincontrol.screens.StatisticsScreen
import com.heungjun.gaincontrol.pages.LoginPage // Import LoginPage

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel // 추가
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(route = "login") { // Add login route
            LoginPage(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(route = BottomBarScreen.Home.route) {
            HomePage(
                navController = navController,
                authViewModel = authViewModel // 전달
            )
        }
        composable(route = BottomBarScreen.Statistics.route) {
            StatisticsScreen(
                navController = navController,
                authViewModel = authViewModel // 전달
            )
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(
                navController = navController,
                authViewModel = authViewModel // 전달
            )
        }
    }
}