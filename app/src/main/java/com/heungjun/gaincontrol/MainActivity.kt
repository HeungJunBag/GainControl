package com.heungjun.gaincontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.heungjun.gaincontrol.navigation.MyAppNavigation
import com.heungjun.gaincontrol.ui.theme.GainControlTheme
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.jakewharton.threetenabp.AndroidThreeTen // ThreeTenABP 초기화를 위한 import

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ThreeTenABP 초기화
        AndroidThreeTen.init(this)

        // Compose UI 설정
        setContent {
            GainControlTheme {
                MyAppNavigation(
                    modifier = Modifier.fillMaxSize(),
                    authViewModel = authViewModel
                )
            }
        }
    }
}
