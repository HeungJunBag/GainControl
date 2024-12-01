package com.heungjun.gaincontrol.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()

    // 인증되지 않은 경우 로그인 화면으로 리디렉션
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo(0) // 백스택 비우기
            }
        }
    }

    val settingsOptions = listOf(
        "프로필 설정",
        "개인정보 및 보안",
        "알림",
        "언어",
        "앱 정보"
    )

    GradientBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 제목
            item {
                Text(
                    text = "설정",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 설정 옵션
            items(settingsOptions) { option ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
//                backgroundColor = Color.LightGray
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

            // 간격 추가
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 로그아웃 버튼
            item {
                Button(
                    onClick = { authViewModel.signout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0047AB), // 기본 색상: 코발트 블루
                        contentColor = Color.White,        // 텍스트 색상: 화이트
                        disabledContainerColor = Color(0xFFB0C4DE), // 비활성화 색상: 연한 파란색
                        disabledContentColor = Color(0xFFD3D3D3)    // 비활성화 텍스트 색상: 연한 회색
                    ),
                    shape = RoundedCornerShape(12.dp) // 버튼 모서리 둥글게
                ) {
                    Text(
                        text = "로그아웃",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // 제목
//        item {
//            Text(
//                text = "설정",
//                fontSize = 24.sp,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//        }
//
//        // 설정 옵션
//        items(settingsOptions) { option ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp),
////                backgroundColor = Color.LightGray
//            ) {
//                Text(
//                    text = option,
//                    fontSize = 18.sp,
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                )
//            }
//        }
//
//        // 간격 추가
//        item {
//            Spacer(modifier = Modifier.height(20.dp))
//        }
//
//        // 로그아웃 버튼
//        item {
//            Button(
//                onClick = { authViewModel.signout() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 8.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF0047AB), // 커스텀 버튼 색상 (예: Tomato 색상)
//                    contentColor = Color.White         // 텍스트 색상
//                ),
//                shape = RoundedCornerShape(12.dp)     // 둥근 모서리
//            ) {
//                Text(
//                    text = "로그아웃",
//                    fontSize = 18.sp
//                )
//            }
//        }
//    }
}




