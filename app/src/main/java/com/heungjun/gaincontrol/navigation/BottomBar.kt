package com.heungjun.gaincontrol.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Profile,
        BottomBarScreen.Settings,
    )
    BottomNavigation {
        screens.forEach { screen ->
            BottomNavigationItem(
                label = { androidx.compose.material.Text(text = screen.title) },
                icon = { androidx.compose.material.Icon(imageVector = screen.icon, contentDescription = screen.title) },
                selected = navController.currentBackStackEntryAsState()?.value?.destination?.hierarchy
                    ?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}