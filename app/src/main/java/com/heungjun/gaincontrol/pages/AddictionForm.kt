package com.heungjun.gaincontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

@Composable
fun AddictionForm(navController: NavController) {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "중독 습관 입력",
                fontSize = 24.sp,
                color = Color.Black
            )
        }

        // 카테고리 선택 체크박스
        item {
            Text("카테고리를 선택하세요", fontSize = 16.sp, color = Color.Gray)
        }
        item {
            Column {
                CheckboxWithLabel(
                    label = "흡연",
                    isChecked = isSmokingSelected,
                    onCheckedChange = { isSmokingSelected = it }
                )
                CheckboxWithLabel(
                    label = "게임",
                    isChecked = isGamingSelected,
                    onCheckedChange = { isGamingSelected = it }
                )
                CheckboxWithLabel(
                    label = "음주",
                    isChecked = isDrinkingSelected,
                    onCheckedChange = { isDrinkingSelected = it }
                )
            }
        }

        // 담배 입력 폼
        if (isSmokingSelected) {
            item {
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

        // 게임 입력 폼
        if (isGamingSelected) {
            item {
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

        // 술 입력 폼
        if (isDrinkingSelected) {
            item {
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
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("제출")
            }
        }
    }
}

@Composable
fun CheckboxWithLabel(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(label)
    }
}

@Composable
fun TextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
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
