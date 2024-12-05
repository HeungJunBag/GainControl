package com.heungjun.gaincontrol.pages

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineGraphWithModal(
    data: List<Float>, // 그래프 데이터
    label: String = "", // 그래프 라벨
    modifier: Modifier = Modifier
) {
    // 선택 상태 관리
    var showModal by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("선택 없음") }
    var selectedType by remember { mutableStateOf("선택 없음") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 그래프
        LineGraphMPAndroidChart(
            data = data,
            label = label,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.TopCenter)
        )

        // 플로팅 버튼
        FloatingActionButton(
            onClick = { showModal = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }

        // 모달 창
        if (showModal) {
            AlertDialog(
                onDismissRequest = { showModal = false },
                title = { Text("항목 선택") },
                text = {
                    Column {
                        Text("카테고리 선택")
                        DropdownMenuSelection(
                            items = listOf("게임", "음주", "담배"),
                            selected = selectedCategory,
                            onSelect = { selectedCategory = it }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("타입 선택")
                        DropdownMenuSelection(
                            items = listOf("시간", "돈"),
                            selected = selectedType,
                            onSelect = { selectedType = it }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showModal = false }) {
                        Text("확인")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showModal = false }) {
                        Text("취소")
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }

        // 선택된 값 표시
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("선택된 카테고리: $selectedCategory")
            Text("선택된 타입: $selectedType")
        }
    }
}

@Composable
fun DropdownMenuSelection(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun LineGraphMPAndroidChart(
    data: List<Float>, // 그래프 데이터
    label: String = "", // 그래프 라벨
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                axisLeft.textColor = Color.BLACK
                xAxis.textColor = Color.BLACK
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }
            val lineDataSet = LineDataSet(entries, label).apply {
                color = Color.BLUE
                valueTextColor = Color.GRAY
                lineWidth = 2f
                circleRadius = 4f
                circleColors = listOf(Color.BLUE)
                setDrawCircleHole(false)
            }
            chart.data = LineData(lineDataSet)
            chart.xAxis.axisMinimum = 0f
            chart.xAxis.axisMaximum = (data.size - 1).toFloat()
            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.axisMaximum = (data.maxOrNull() ?: 0f) + 10f
            chart.invalidate()
        }
    )
}

@Composable
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
fun LineGraphWithModalPreview() {
    LineGraphWithModal(
        data = listOf(10f, 20f, 30f, 40f, 50f),
        label = "데모 그래프"
    )
}
