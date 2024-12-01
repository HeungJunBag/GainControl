package com.heungjun.gaincontrol.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Statistics : BottomBarScreen(
        route = "statistics",
        title = "Statistics",
        icon = Icons.Default.BarChart
    )
    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}