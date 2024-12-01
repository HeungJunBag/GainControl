package com.heungjun.gaincontrol.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // 비밀번호 확인 필드 추가
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // 상태에 따라 화면 전환
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    // 전체 배경
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF7E5F), // 오렌지 핑크
                        Color(0xFFFFA372), // 연한 오렌지
                        Color(0xFFFFD056)  // 밝은 노란색
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 제목
            Text(
                text = "Signup",
                fontSize = 32.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 이메일 입력 필드
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "이메일을 입력하세요", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
//                    textColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 비밀번호 입력 필드
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "비밀번호를 입력하세요", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
//                    textColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 비밀번호 확인 입력 필드
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "비밀번호를 한번 더 입력하세요", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
//                    textColor = Color.White
                )
            )

            // 비밀번호 일치 여부 메시지
            val passwordsMatch = password == confirmPassword
            Text(
                text = when {
                    confirmPassword.isEmpty() -> "" // 비밀번호 확인 필드가 비어있으면 메시지 표시 안 함
                    passwordsMatch -> "비밀번호가 일치합니다" // 일치하면 연두색 메시지
                    else -> "비밀번호가 일치하지 않습니다" // 일치하지 않으면 빨간색 메시지
                },
                color = if (passwordsMatch) Color(0xFF4CAF50) else Color(0xFFFF5722), // 연두색 또는 빨간색
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 회원가입 버튼
            Button(
                onClick = {
                    if (passwordsMatch) {
                        authViewModel.signup(email, password)
                    } else {
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8008))
            ) {
                Text(
                    text = "계정 생성",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 로그인 페이지로 이동 버튼
            TextButton(onClick = { navController.navigate("login") }) {
                Text(
                    text = "이미 계정이 있으신가요? 로그인 하러 가기",
                    color = Color.White
                )
            }
        }
    }
}















@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun SignupPagePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF7E5F), // 오렌지 핑크
                        Color(0xFFFFA372), // 연한 오렌지
                        Color(0xFFFFD056)  // 밝은 노란색
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "회원가입",
                fontSize = 32.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "이메일을 입력하세요", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "비밀번호를 입력하세요", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "비밀번호를 한번 더 입력하세요", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8008))
            ) {
                Text(
                    text = "계정 생성",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {}) {
                Text(
                    text = "이미 계정이 있으신가요? 로그인 하러 가기",
                    color = Color.White
                )
            }
        }
    }
}