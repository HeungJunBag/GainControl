package com.heungjun.gaincontrol.pages

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.heungjun.gaincontrol.viewmodel.HealthGoalsViewModel

@Composable
fun LineGraphMPAndroidChart(
    data: List<Float>, // 그래프 데이터
    label: String = "", // 그래프 라벨
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth() // 그래프 가로 길이 채우기
            .height(300.dp), // 그래프 높이 설정
        factory = { context ->
            LineChart(context).apply {
                // LineChart 기본 설정
                description.isEnabled = false // 설명 제거
                xAxis.position = XAxis.XAxisPosition.BOTTOM // X축 아래로 위치
                axisRight.isEnabled = false // 오른쪽 Y축 비활성화
                axisLeft.textColor = Color.BLACK // 왼쪽 Y축 텍스트 색상
                xAxis.textColor = Color.BLACK // X축 텍스트 색상
                legend.isEnabled = true // 그래프 설명 활성화
            }
        },
        update = { chart ->
            // 데이터셋 생성
            val entries = data.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }
            val lineDataSet = LineDataSet(entries, label).apply {
                color = Color.BLUE // 그래프 선 색상
                valueTextColor = Color.GRAY // 데이터 값 텍스트 색상
                lineWidth = 2f // 그래프 선 두께
                circleRadius = 4f // 데이터 포인트 원 크기
                circleColors = listOf(Color.BLUE) // 데이터 포인트 원 색상
                setDrawCircleHole(false) // 데이터 포인트 원 내부 비우기
            }

            // 데이터 추가
            chart.data = LineData(lineDataSet)

            // 축 범위 설정
            chart.xAxis.axisMinimum = 0f
            chart.xAxis.axisMaximum = (data.size - 1).toFloat()
            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.axisMaximum = (data.maxOrNull() ?: 0f) + 10f // 최대값 + 여유값

            // 그래프 업데이트
            chart.invalidate()
        }
    )
}

@Composable
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
fun LineGraphPreview() {
    LineGraphMPAndroidChart(
        data = listOf(10f, 20f, 30f, 40f, 50f), // 샘플 데이터
        label = "데모 그래프"
    )
}
