package com.heungjun.gaincontrol.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // 사용자 이메일 가져오기
    val userEmail = authViewModel.getUserEmail()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
    }

    var isNotificationEnabled by remember { mutableStateOf(false) }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 프로필 섹션
            ProfileSection()

            // 사용자 이메일 섹션
            UserEmailSection(userEmail)

            // 공유 기능
            ShareSection { shareContent(context) }

            // 알림 토글 설정
            NotificationToggleItem(
                isNotificationEnabled = isNotificationEnabled,
                onToggleChanged = { isNotificationEnabled = it }
            )

            // 로그아웃 버튼
            LogoutButton {
                authViewModel.signout()
            }
        }
    }
}

@Composable
fun ProfileSection() {
    Card(
        modifier = Modifier
            .size(120.dp) // 카드 크기
            .padding(top = 8.dp),
        shape = CircleShape, // 원형 카드
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)), // 연한 초록색 배경
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "프로필 이미지",
            tint = Color(0xFF388E3C), // 진한 초록색
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun UserEmailSection(userEmail: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "사용자 이메일",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = userEmail,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32) // 진한 초록색
        )
    }
}

@Composable
fun ShareSection(onShareClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)), // 연한 초록색 배경
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "공유",
                fontSize = 18.sp,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.weight(1f)) // 텍스트와 아이콘 간격
            Button(
                onClick = onShareClicked,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("공유하기", color = Color.White)
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
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(8.dp)
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
                fontSize = 18.sp,
                color = Color(0xFF2E7D32)
            )
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = onToggleChanged,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF4CAF50),
                    uncheckedThumbColor = Color(0xFF81C784),
                    checkedTrackColor = Color(0xFF388E3C),
                    uncheckedTrackColor = Color(0xFFB2DFDB)
                )
            )
        }
    }
}

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = "로그아웃", fontSize = 18.sp)
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