package com.heungjun.gaincontrol.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun DetailedCalendarApp() {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showInputDialog by remember { mutableStateOf(false) }
    val plans = remember { mutableStateMapOf<String, MutableList<Plan>>() }
    val accumulatedData = remember { mutableStateMapOf<String, MutableMap<String, Int>>() }

    var TotalDambe by remember { mutableStateOf(0) }
    var TotalSoju by remember { mutableStateOf(0) }
    var TotalGame by remember { mutableStateOf(0) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // 캘린더 화면
        CalendarScreen(
            selectedDate = selectedDate,
            onDateChange = { newDate ->
                selectedDate = newDate
                showInputDialog = true
            },
            plans = plans
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 날짜별 계획 표시
        val selectedDateKey = getDateKey(selectedDate)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // 날짜 표시
            Text(
                text = "${selectedDate.get(Calendar.YEAR)}년 ${selectedDate.get(Calendar.MONTH) + 1}월 ${selectedDate.get(Calendar.DAY_OF_MONTH)}일",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 선택한 날짜의 계획 목록 표시
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

            Spacer(modifier = Modifier.height(16.dp))

            // Total 값 표시
            Text(
                text = "누적 총합 데이터",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = "총 담배 수: $TotalDambe", modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "총 예상 주량: $TotalSoju", modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "총 게임 시간: $TotalGame 시간", modifier = Modifier.padding(vertical = 4.dp))
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

                // 누적 데이터 업데이트
                updateAccumulatedData(accumulatedData, category, details)

                // Total 값 업데이트
                when (category) {
                    "담배" -> {
                        val todayDambe = details.lines().find { it.startsWith("avgPacks:") }
                            ?.split(":")
                            ?.get(1)
                            ?.trim()
                            ?.toIntOrNull() ?: 0
                        TotalDambe += todayDambe
                    }
                    "술" -> {
                        val todaySoju = details.lines().find { it.startsWith("estimatedAmount:") }
                            ?.split(":")
                            ?.get(1)
                            ?.trim()
                            ?.toIntOrNull() ?: 0
                        TotalSoju += todaySoju
                    }
                    "게임" -> {
                        val startTime = details.lines().find { it.startsWith("startTime:") }
                            ?.split(":")
                            ?.get(1)
                            ?.trim()
                            ?.toIntOrNull() ?: 0
                        val endTime = details.lines().find { it.startsWith("endTime:") }
                            ?.split(":")
                            ?.get(1)
                            ?.trim()
                            ?.toIntOrNull() ?: 0
                        val todayGame = if (endTime > startTime) endTime - startTime else 0
                        TotalGame += todayGame
                    }
                }

                showInputDialog = false
            }
        )
    }
}


fun updateAccumulatedData(
    accumulatedData: MutableMap<String, MutableMap<String, Int>>,
    category: String,
    details: String
) {
    // 세부 정보를 Map으로 변환
    val detailMap = details.lines()
        .mapNotNull { line ->
            val parts = line.split(":").map { it.trim() }
            if (parts.size == 2) {
                val key = parts[0]
                val value = parts[1].toIntOrNull() // 숫자만 처리
                if (value != null) key to value else null
            } else null
        }.toMap()

    // 카테고리에 해당하는 누적 데이터 가져오기 (없으면 생성)
    val categoryData = accumulatedData.getOrPut(category) { mutableMapOf() }

    // 누적 데이터를 업데이트
    detailMap.forEach { (key, value) ->
        categoryData[key] = categoryData.getOrDefault(key, 0) + value
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
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    calendar.add(Calendar.MONTH, -1)
                    onDateChange(calendar)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "이전 달",
                    tint = Color(0xFF4CAF50)
                )
            }

            Text(
                text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00796B)
                )
            )

            IconButton(
                onClick = {
                    calendar.add(Calendar.MONTH, 1)
                    onDateChange(calendar)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "다음 달",
                    tint = Color(0xFF4CAF50)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            // 빈 칸 생성 (월 시작 요일에 맞게)
            items((firstDayOfWeek - 1).coerceAtLeast(0)) {
                Spacer(modifier = Modifier.size(40.dp))
            }

            // 날짜 표시
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isSelected -> Color(0xFF4CAF50) // 선택된 날짜 강조
                                hasPlans -> Color(0xFFA5D6A7) // 일정이 있는 날짜 강조
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
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DetailedPlanInputDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("게임") }
    val inputValues = remember { mutableStateMapOf<String, String>() }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                "\uD83D\uDDD3\uFE0F",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFFF1F8E9)) // 연한 녹색 배경
                    .clip(RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                // 카테고리 선택
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("담배", "술", "게임").forEach { category ->
                        TextButton(
                            onClick = { selectedCategory = category },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (selectedCategory == category) Color(0xFF4CAF50) else Color.Gray
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedCategory == category) Color(0xFFA5D6A7) else Color.Transparent
                                )
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 선택된 카테고리에 따라 질문 표시
                when (selectedCategory) {
                    "담배" -> {
                        TextFieldWithLabel(
                            label = "하루에 평균 몇갑 피우시나요?",
                            value = inputValues["avgPacks"] ?: "",
                            onValueChange = { inputValues["avgPacks"] = it }
                        )
                        TextFieldWithLabel(
                            label = "일반적으로 매일 몇시간을 소비하시나요?",
                            value = inputValues["dailyTime"] ?: "",
                            onValueChange = { inputValues["dailyTime"] = it }
                        )
                        TextFieldWithLabel(
                            label = "매주 평균적으로 얼마만큼의 비용을 지출하시나요?",
                            value = inputValues["weeklyCost"] ?: "",
                            onValueChange = { inputValues["weeklyCost"] = it }
                        )
                    }
                    "술" -> {
                        TextFieldWithLabel(
                            label = "음주 시작 시간",
                            value = inputValues["startTime"] ?: "",
                            onValueChange = { inputValues["startTime"] = it }
                        )
                        TextFieldWithLabel(
                            label = "종료 시간",
                            value = inputValues["endTime"] ?: "",
                            onValueChange = { inputValues["endTime"] = it }
                        )
                        TextFieldWithLabel(
                            label = "예상 주량",
                            value = inputValues["estimatedAmount"] ?: "",
                            onValueChange = { inputValues["estimatedAmount"] = it }
                        )
                        TextFieldWithLabel(
                            label = "매주 평균적으로 얼마만큼 지출하시나요?",
                            value = inputValues["weeklyCost"] ?: "",
                            onValueChange = { inputValues["weeklyCost"] = it }
                        )
                    }
                    "게임" -> {
                        TextFieldWithLabel(
                            label = "게임 시작 시간",
                            value = inputValues["startTime"] ?: "",
                            onValueChange = { inputValues["startTime"] = it }
                        )
                        TextFieldWithLabel(
                            label = "종료 시간",
                            value = inputValues["endTime"] ?: "",
                            onValueChange = { inputValues["endTime"] = it }
                        )
                        TextFieldWithLabel(
                            label = "매주 평균적으로 얼마나 하시나요?",
                            value = inputValues["weeklyPlay"] ?: "",
                            onValueChange = { inputValues["weeklyPlay"] = it }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val details = inputValues.entries.joinToString("\n") { "${it.key}: ${it.value}" }
                    onSave(selectedCategory, details)
                },
                colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    text = "저장",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFFB0BEC5))
            ) {
                Text(
                    text = "취소",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        // 라벨
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 텍스트 필드
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)) // 둥근 모서리
                .background(Color(0xFFFAFAFA)), // 배경색
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black
            ),
            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                containerColor = Color.White, // 텍스트 필드 배경색
                focusedIndicatorColor = Color(0xFF4CAF50), // 포커스 상태의 경계선 색상
                unfocusedIndicatorColor = Color.Gray, // 비포커스 상태의 경계선 색상
                cursorColor = Color(0xFF4CAF50) // 커서 색상
            )
        )
    }
}

// 세부 정보를 키 순서에 따라 정렬하는 함수
fun sortPlanDetails(details: String): String {
    val detailMap = details.lines()
        .map { line -> line.split(":").let { it[0] to it.getOrElse(1) { "" } } }
        .toMap()

    val sortedKeys = listOf("startTime", "endTime", "weeklyPlay", "avgPacks", "dailyTime", "weeklyCost", "estimatedAmount")
    return sortedKeys.mapNotNull { key ->
        detailMap[key]?.let { translateKeyToKorean("$key:$it") }
    }.joinToString("\n")
}

// 키를 한글로 변환하는 함수
fun translateKeyToKorean(detail: String): String {
    return when {
        detail.startsWith("startTime:") -> detail.replace("startTime:", "시작 시간:")
        detail.startsWith("endTime:") -> detail.replace("endTime:", "종료 시간:")
        detail.startsWith("weeklyPlay:") -> detail.replace("weeklyPlay:", "주간 플레이 시간:")
        detail.startsWith("avgPacks:") -> detail.replace("avgPacks:", "평균 담배 갑 수:")
        detail.startsWith("dailyTime:") -> detail.replace("dailyTime:", "하루 소비 시간:")
        detail.startsWith("weeklyCost:") -> detail.replace("weeklyCost:", "주간 비용:")
        detail.startsWith("estimatedAmount:") -> detail.replace("estimatedAmount:", "예상 주량:")
        else -> detail
    }
}

// 데이터 클래스
data class Plan(val category: String, val details: String)

// 날짜 키 생성 함수
fun getDateKey(date: Calendar): String {
    return "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailedCalendarApp() {
    DetailedCalendarApp()
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendarScreen() {
    val selectedDate = Calendar.getInstance()
    val plans = mapOf(
        getDateKey(selectedDate) to listOf(
            Plan("게임", "startTime:14\nendTime:16\nweeklyPlay:5"),
            Plan("술", "startTime:18\nendTime:22\nestimatedAmount:2")
        )
    )
    CalendarScreen(
        selectedDate = selectedDate,
        onDateChange = {},
        plans = plans
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailedPlanInputDialog() {
    DetailedPlanInputDialog(
        onDismiss = {},
        onSave = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTextFieldWithLabel() {
    TextFieldWithLabel(
        label = "라벨 텍스트",
        value = "기본 값",
        onValueChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPlanDetails() {
    Text(
        text = sortPlanDetails(
            """
            startTime:14
            endTime:16
            weeklyPlay:5
            avgPacks:2
            dailyTime:4
            weeklyCost:50
            estimatedAmount:3
            """.trimIndent()
        )
    )
}




