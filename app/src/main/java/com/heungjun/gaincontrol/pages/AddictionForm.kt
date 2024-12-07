package com.heungjun.gaincontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddictionForm(navController: NavController, authViewModel: AuthViewModel) {
    // 각 카테고리 선택 여부
    var isSmokingSelected by remember { mutableStateOf(false) }
    var isGamingSelected by remember { mutableStateOf(false) }
    var isDrinkingSelected by remember { mutableStateOf(false) }

    // 담배 입력 상태
    var smokingStartDate by remember { mutableStateOf("") }
    var smokingPerDay by remember { mutableStateOf("") }
    var smokingGoalYears by remember { mutableStateOf("") }

    // 게임 입력 상태
    var gamingStartDate by remember { mutableStateOf("") }
    var gamingPerWeek by remember { mutableStateOf("") }
    var gamingHoursPerSession by remember { mutableStateOf("") }
    var gamingGoalYears by remember { mutableStateOf("") }

    // 술 입력 상태
    var drinkingStartDate by remember { mutableStateOf("") }
    var drinkingPerWeek by remember { mutableStateOf("") }
    var drinkingAmountPerSession by remember { mutableStateOf("") }
    var drinkingGoalYears by remember { mutableStateOf("") }

    // 모든 입력 폼이 채워졌는지 확인하는 변수
    val isFormValid = remember {
        derivedStateOf {
            (!isSmokingSelected || (smokingStartDate.isNotEmpty() && smokingPerDay.isNotEmpty() && smokingGoalYears.isNotEmpty())) &&
                    (!isGamingSelected || (gamingStartDate.isNotEmpty() && gamingPerWeek.isNotEmpty() && gamingHoursPerSession.isNotEmpty() && gamingGoalYears.isNotEmpty())) &&
                    (!isDrinkingSelected || (drinkingStartDate.isNotEmpty() && drinkingPerWeek.isNotEmpty() && drinkingAmountPerSession.isNotEmpty() && drinkingGoalYears.isNotEmpty()))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚠ 중독 습관 입력", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF4CAF50))
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6F6F6)), // 배경 색상 변경
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 카테고리 선택
                item {
                    Text(
                        text = "카테고리를 선택하세요",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        EnhancedCheckboxWithLabel(
                            label = "흡연",
                            isChecked = isSmokingSelected,
                            onCheckedChange = { isSmokingSelected = it }
                        )
                        EnhancedCheckboxWithLabel(
                            label = "게임",
                            isChecked = isGamingSelected,
                            onCheckedChange = { isGamingSelected = it }
                        )
                        EnhancedCheckboxWithLabel(
                            label = "음주",
                            isChecked = isDrinkingSelected,
                            onCheckedChange = { isDrinkingSelected = it }
                        )
                    }
                }

                // 담배 입력 폼
                if (isSmokingSelected) {
                    item {
                        AddictionCategoryCard("흡연 데이터 입력") {
                            TextFieldWithLabel("언제부터 흡연을 시작했나요? (YYYY-MM-DD)", smokingStartDate) {
                                smokingStartDate = it
                            }
                            TextFieldWithLabel("하루에 몇 개비를 피우나요?", smokingPerDay) {
                                smokingPerDay = it
                            }
                            TextFieldWithLabel("목표 기간 (년)", smokingGoalYears) {
                                smokingGoalYears = it
                            }
                        }
                    }
                }

                // 게임 입력 폼
                if (isGamingSelected) {
                    item {
                        AddictionCategoryCard("게임 데이터 입력") {
                            TextFieldWithLabel("언제부터 게임을 시작했나요? (YYYY-MM-DD)", gamingStartDate) {
                                gamingStartDate = it
                            }
                            TextFieldWithLabel("일주일에 몇 번 게임을 하나요?", gamingPerWeek) {
                                gamingPerWeek = it
                            }
                            TextFieldWithLabel("한 번 할 때 몇 시간을 하나요?", gamingHoursPerSession) {
                                gamingHoursPerSession = it
                            }
                            TextFieldWithLabel("목표 기간 (년)", gamingGoalYears) {
                                gamingGoalYears = it
                            }
                        }
                    }
                }

                // 술 입력 폼
                if (isDrinkingSelected) {
                    item {
                        AddictionCategoryCard("음주 데이터 입력") {
                            TextFieldWithLabel("언제부터 음주를 시작했나요? (YYYY-MM-DD)", drinkingStartDate) {
                                drinkingStartDate = it
                            }
                            TextFieldWithLabel("일주일에 몇 번 술을 마시나요?", drinkingPerWeek) {
                                drinkingPerWeek = it
                            }
                            TextFieldWithLabel("한 번 마실 때 소주 몇 병을 마시나요?", drinkingAmountPerSession) {
                                drinkingAmountPerSession = it
                            }
                            TextFieldWithLabel("목표 기간 (년)", drinkingGoalYears) {
                                drinkingGoalYears = it
                            }
                        }
                    }
                }

                // 제출 버튼
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            if (userId != null) {
                                saveDataToFirebase(
                                    userId,
                                    smokingStartDate,
                                    smokingPerDay,
                                    smokingGoalYears,
                                    gamingStartDate,
                                    gamingPerWeek,
                                    gamingHoursPerSession,
                                    gamingGoalYears,
                                    drinkingStartDate,
                                    drinkingPerWeek,
                                    drinkingAmountPerSession,
                                    drinkingGoalYears
                                )
                                navController.navigate("home") // 저장 후 홈으로 이동
                            }
                        },
                        enabled = isFormValid.value,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp)
                    ) {
                        Text("저장", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    )
}


@Composable
fun EnhancedCheckboxWithLabel(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    padding: Dp = 8.dp // 기본값을 16dp로 설정, 필요에 따라 줄임
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding) // 여기서 패딩을 조정
            .background(
                color = if (isChecked) Color(0xFFE3F2FD) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 1.dp) // 수직 패딩을 줄임
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF4CAF50),
                uncheckedColor = Color.Gray
            )
        )
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
            color = if (isChecked) Color(0xFF1E88E5) else Color(0xFF757575),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4CAF50), // 더 생동감 있는 색상 적용
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // 높이를 고정해 더 세련된 느낌 제공
            shape = RoundedCornerShape(12.dp), // 라운드된 모서리
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF4CAF50), // 포커스된 테두리 색상
                unfocusedBorderColor = Color(0xFFBDBDBD), // 비활성화 상태 테두리 색상
                cursorColor = Color(0xFF4CAF50), // 커서 색상
//                textColor = Color(0xFF37474F), // 텍스트 색상
                containerColor = Color(0xFFF6F6F6) // 배경 색상
            ),
            singleLine = true // 한 줄 입력 필드
        )
    }
}

@Composable
fun AddictionCategoryCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp), // 더 둥근 모서리
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC)), // 연한 배경 색상
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // 약간의 그림자 효과
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 제목
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // 적절한 아이콘
                    contentDescription = null,
                    tint = Color(0xFF4CAF50), // 아이콘 색상
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F),
                    modifier = Modifier.weight(1f)
                )
            }

            // 콘텐츠
            content()
        }
    }
}

private fun saveDataToFirebase(
    userId: String,
    smokingStartDate: String,
    smokingPerDay: String,
    smokingGoalYears: String,
    gamingStartDate: String,
    gamingPerWeek: String,
    gamingHoursPerSession: String,
    gamingGoalYears: String,
    drinkingStartDate: String,
    drinkingPerWeek: String,
    drinkingAmountPerSession: String,
    drinkingGoalYears: String
) {
    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("users").child(userId)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val data = mutableMapOf<String, Any>()

    // 담배 데이터 저장
    if (smokingStartDate.isNotEmpty() && smokingPerDay.isNotEmpty()) {
        val startDate = LocalDate.parse(smokingStartDate, formatter)
        val days = ChronoUnit.DAYS.between(startDate, LocalDate.now()).toInt()
        val totalCigarettes = (smokingPerDay.toIntOrNull() ?: 0) * days
        data["smoking"] = mapOf(
            "startDate" to smokingStartDate,
            "perDay" to smokingPerDay.toIntOrNull(),
            "goalYears" to smokingGoalYears.toIntOrNull(),
            "totalCigarettes" to totalCigarettes
        )
    }

    // 게임 데이터 저장
    if (gamingStartDate.isNotEmpty() && gamingPerWeek.isNotEmpty() && gamingHoursPerSession.isNotEmpty()) {
        val startDate = LocalDate.parse(gamingStartDate, formatter)
        val weeks = ChronoUnit.WEEKS.between(startDate, LocalDate.now()).toInt()
        val totalGamingHours = (gamingPerWeek.toIntOrNull() ?: 0) * (gamingHoursPerSession.toIntOrNull() ?: 0) * weeks
        data["gaming"] = mapOf(
            "startDate" to gamingStartDate,
            "perWeek" to gamingPerWeek.toIntOrNull(),
            "hoursPerSession" to gamingHoursPerSession.toIntOrNull(),
            "goalYears" to gamingGoalYears.toIntOrNull(),
            "totalGamingHours" to totalGamingHours
        )
    }

    // 음주 데이터 저장
    if (drinkingStartDate.isNotEmpty() && drinkingPerWeek.isNotEmpty() && drinkingAmountPerSession.isNotEmpty()) {
        val startDate = LocalDate.parse(drinkingStartDate, formatter)
        val weeks = ChronoUnit.WEEKS.between(startDate, LocalDate.now()).toInt()
        val totalDrinks = (drinkingPerWeek.toIntOrNull() ?: 0) * (drinkingAmountPerSession.toIntOrNull() ?: 0) * weeks
        data["drinking"] = mapOf(
            "startDate" to drinkingStartDate,
            "perWeek" to drinkingPerWeek.toIntOrNull(),
            "amountPerSession" to drinkingAmountPerSession.toIntOrNull(),
            "goalYears" to drinkingGoalYears.toIntOrNull(),
            "totalDrinks" to totalDrinks
        )
    }

    userRef.setValue(data)
        .addOnSuccessListener { println("Data saved successfully!") }
        .addOnFailureListener { exception -> println("Failed to save data: ${exception.message}") }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddictionFormPreview() {
    val navController = rememberNavController() // 더미 NavController를 사용하여 프리뷰 가능

    // AddictionForm 호출
    AddictionForm(navController = navController, authViewModel = AuthViewModel())
}