package com.heungjun.gaincontrol.navigation

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Statistics,
        BottomBarScreen.Home,
        BottomBarScreen.Settings,
    )

    BottomNavigation(
        modifier = Modifier.background(Color(0xFFE8F5E9)), // 배경색 설정
        backgroundColor = Color(0xFFE8F5E9), // 바텀 바 전체 배경색
        contentColor = Color(0xFF4CAF50) // 아이콘과 텍스트 기본 색상
    ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                label = {
                    Text(
                        text = screen.title,
                        color = if (isSelected(navController, screen.route)) Color(0xFF388E3C) else Color.Gray
                    )
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        tint = if (isSelected(navController, screen.route)) Color(0xFF4CAF50) else Color.Gray
                    )
                },
                selected = isSelected(navController, screen.route),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
private fun isSelected(navController: NavHostController, route: String): Boolean {
    return navController.currentBackStackEntryAsState()?.value?.destination?.hierarchy
        ?.any { it.route == route } == true
}