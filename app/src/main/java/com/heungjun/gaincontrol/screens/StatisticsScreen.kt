package com.heungjun.gaincontrol.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.pages.DetailedPlanInputDialog
import com.heungjun.gaincontrol.pages.sortPlanDetails
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import java.util.*

@Composable
fun StatisticsScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val authState = authViewModel.authState.observeAsState()

    // 로그인 상태 확인 후 리디렉션
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("statistics") { inclusive = true }
            }
        }
    }

    // State variables for calendar and plans
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val plans = remember { mutableStateMapOf<String, MutableList<Plan>>() }
    var showInputDialog by remember { mutableStateOf(false) }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 캘린더 헤더
            Text(
                text = "STATISTICS",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 캘린더 화면
            CalendarScreen(
                selectedDate = selectedDate,
                onDateChange = { newDate ->
                    selectedDate = newDate
                    showInputDialog = true
                },
                plans = plans // 일정 데이터 전달
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 선택된 날짜와 계획 표시
            val selectedDateKey = getDateKey(selectedDate)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${selectedDate.get(Calendar.YEAR)}년 ${selectedDate.get(Calendar.MONTH) + 1}월 ${selectedDate.get(Calendar.DAY_OF_MONTH)}일",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val datePlans = plans[selectedDateKey] ?: emptyList()
                if (datePlans.isEmpty()) {
                    Text(text = "일정이 없습니다.", color = Color.Gray)
                } else {
                    datePlans.forEach { plan ->
                        Text(
                            text = buildString {
                                append("${plan.category} -\n")
                                val sortedDetails = sortPlanDetails(plan.details)
                                append(sortedDetails)
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 입력 다이얼로그
        if (showInputDialog) {
            DetailedPlanInputDialog(
                onDismiss = { showInputDialog = false },
                onSave = { category, details ->
                    val dateKey = getDateKey(selectedDate)
                    if (plans[dateKey] == null) {
                        plans[dateKey] = mutableListOf()
                    }
                    plans[dateKey]?.add(Plan(category, details))
                    showInputDialog = false
                }
            )
        }
    }
}

@Composable
fun CalendarScreen(
    selectedDate: Calendar,
    onDateChange: (Calendar) -> Unit,
    plans: Map<String, List<Plan>>
) {
    val year = selectedDate.get(Calendar.YEAR)
    val month = selectedDate.get(Calendar.MONTH)

    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                calendar.add(Calendar.MONTH, -1)
                onDateChange(calendar)
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "이전 달")
            }

            Text(
                text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            IconButton(onClick = {
                calendar.add(Calendar.MONTH, 1)
                onDateChange(calendar)
            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "다음 달")
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // 고정 높이 설정
        ) {
            items((firstDayOfWeek - 1).coerceAtLeast(0)) {
                Spacer(modifier = Modifier.size(40.dp))
            }

            items(maxDay) { day ->
                val currentDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day + 1)
                }
                val dateKey = getDateKey(currentDate)
                val hasPlans = plans.containsKey(dateKey)

                val isSelected = (day + 1 == selectedDate.get(Calendar.DAY_OF_MONTH)
                        && month == selectedDate.get(Calendar.MONTH)
                        && year == selectedDate.get(Calendar.YEAR))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            when {
                                isSelected -> Color(0xFF357EDD)
                                hasPlans -> Color(0xFF00BCD4)
                                else -> Color.Transparent
                            }
                        )
                        .clickable {
                            val newDate = Calendar.getInstance().apply {
                                set(Calendar.YEAR, year)
                                set(Calendar.MONTH, month)
                                set(Calendar.DAY_OF_MONTH, day + 1)
                            }
                            onDateChange(newDate)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${day + 1}",
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

// 데이터 클래스와 도우미 함수
data class Plan(val category: String, val details: String)

fun getDateKey(date: Calendar): String {
    return "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
}
