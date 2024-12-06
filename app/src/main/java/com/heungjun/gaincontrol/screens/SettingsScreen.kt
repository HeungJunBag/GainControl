package com.heungjun.gaincontrol.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current // 현재 컨텍스트 가져오기

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
    }

    val settingsOptions = listOf(
        "프로필 설정",
        "공유",
        "알림"
    )

    // 알림 토글 상태
    var isNotificationEnabled by remember { mutableStateOf(false) }

    GradientBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "설정",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(settingsOptions) { option ->
                if (option == "알림") {
                    NotificationToggleItem(
                        isNotificationEnabled = isNotificationEnabled,
                        onToggleChanged = { isNotificationEnabled = it }
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        onClick = {
                            when (option) {
                                "공유" -> {
                                    // 공유 화면 처리
                                    shareContent(context)
                                }
                                "프로필 설정" -> { /* 프로필 설정 화면 처리 */ }
                            }
                        }
                    ) {
                        Text(
                            text = option,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Button(
                    onClick = { authViewModel.signout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // 초록색 배경
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "로그아웃", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun NotificationToggleItem(
    isNotificationEnabled: Boolean,
    onToggleChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "알림",
                fontSize = 18.sp
            )
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = onToggleChanged,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF4CAF50), // 초록색 토글 버튼
                    uncheckedThumbColor = Color(0xFF81C784), // 연한 초록색
                    checkedTrackColor = Color(0xFF388E3C), // 초록색 트랙
                    uncheckedTrackColor = Color(0xFFB2DFDB) // 연한 초록 트랙
                )
            )
        }
    }
}

fun shareContent(context: android.content.Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "이 앱을 공유합니다!")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "공유할 앱 선택"))
}
