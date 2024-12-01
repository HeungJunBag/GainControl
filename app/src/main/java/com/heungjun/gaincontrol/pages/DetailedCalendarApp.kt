package com.heungjun.gaincontrol.pages

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun DetailedCalendarApp() {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showInputDialog by remember { mutableStateOf(false) }
    val plans = remember { mutableStateMapOf<String, MutableList<Plan>>() }

    // 스크롤 상태 기억
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // 스크롤 가능
    ) {
        // 캘린더 화면
        CalendarScreen(
            selectedDate = selectedDate,
            onDateChange = { newDate ->
                selectedDate = newDate
                showInputDialog = true
            },
            plans = plans // 추가: 일정 데이터 전달
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
                .aspectRatio(1f)
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
                                hasPlans -> Color(0xFF00BCD4) // 일정이 있는 날을 하이라이트 (연한 파랑)
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

@Composable
fun DetailedPlanInputDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("게임") }
    val inputValues = remember { mutableStateMapOf<String, String>() }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("세부 일정") },
        text = {
            Column {
                // 카테고리 선택
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("담배", "술", "게임").forEach { category ->
                        TextButton(
                            onClick = { selectedCategory = category },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (selectedCategory == category) Color.Blue else Color.Gray
                            )
                        ) {
                            Text(category)
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
            TextButton(onClick = {
                val details = inputValues.entries.joinToString("\n") { "${it.key}: ${it.value}" }
                onSave(selectedCategory, details)
            }) {
                Text("저장")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("취소")
            }
        }
    )
}

@Composable
fun TextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
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

