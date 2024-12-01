package com.heungjun.gaincontrol.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    GradientBackground {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Home Page", fontSize = 32.sp)

//        TextButton(onClick = {
//            authViewModel.signout()
//        }) {
//            Text(text = "Sign out")
//        }
        }
    }

}