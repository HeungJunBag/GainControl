package com.heungjun.gaincontrol.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun LineChartComponent(
    label: String,
    data: List<Double>,
    modifier: Modifier = Modifier,
    xAxisLabelCount: Int? = null, // X축 라벨 개수 옵션
    yAxisLabel: String? = null // Y축 단위
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                // 데이터 설정
                val entries = data.mapIndexed { index, value ->
                    Entry(index.toFloat() + 1, value.toFloat()) // X값은 1부터 시작
                }

                val dataSet = LineDataSet(entries, label).apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                    valueTextSize = 10f
                    lineWidth = 2f
                    setCircleColor(Color.RED)
                    setDrawCircles(true)
                    setDrawValues(false) // 그래프 위에 값 표시 여부
                }

                this.data = LineData(dataSet)
                description.isEnabled = false // 차트 설명 비활성화

                // X축 설정
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f // 최소 간격
                    axisMinimum = 1f
                    axisMaximum = (xAxisLabelCount ?: data.size).toFloat() // 데이터 크기에 따라 설정
                    labelCount = xAxisLabelCount ?: data.size
                    textSize = 12f
                }

                // Y축 설정
                axisLeft.apply {
                    setDrawGridLines(true)
                    granularity = 1f // 최소 간격
                    textSize = 12f
                    if (yAxisLabel != null) {
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "${value.toInt()} $yAxisLabel"
                            }
                        }
                    }
                }
                axisRight.isEnabled = false // 오른쪽 Y축 비활성화

                legend.textSize = 12f // 범례 텍스트 크기
                invalidate() // 그래프 다시 그리기
            }
        },
        modifier = modifier
    )
}
