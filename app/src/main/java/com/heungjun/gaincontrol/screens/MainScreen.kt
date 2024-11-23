package com.heungjun.gaincontrol.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.heungjun.gaincontrol.navigation.BottomBar
import com.heungjun.gaincontrol.navigation.BottomNavGraph
import com.heungjun.gaincontrol.viewmodel.AuthViewModel

@Composable
fun MainScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        BottomNavGraph(
            navController = navController,
            paddingValues = innerPadding,
            authViewModel = authViewModel // 전달
        )
    }
}