package com.heungjun.gaincontrol

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.google.firebase.messaging.FirebaseMessaging
import com.heungjun.gaincontrol.navigation.MyAppNavigation
import com.heungjun.gaincontrol.ui.theme.GainControlTheme
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.jakewharton.threetenabp.AndroidThreeTen // ThreeTenABP 초기화를 위한 import

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ThreeTenABP 초기화
        AndroidThreeTen.init(this)

        // 알림 권한 요청(Android 13 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        // Firebase 토큰 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Toast.makeText(this, "Token: $token", Toast.LENGTH_SHORT).show()
                Log.d("FCM", "Token: $token")
            } else {
                Log.e("FCM", "Fetching FCM registration token failed", task.exception)
            }
        }

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
