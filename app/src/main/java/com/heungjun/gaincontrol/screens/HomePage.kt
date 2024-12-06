package com.heungjun.gaincontrol.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heungjun.gaincontrol.components.LineChartComponent
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.viewmodel.UserViewModel

@Composable
fun HomePage(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = viewModel()
) {
    val graphData = userViewModel.graphData.observeAsState()

    // 스크롤 상태 추가
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // 스크롤 가능하도록 설정
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 흡연 관련 그래프 (데이터가 있을 때만 제목과 그래프 표시)
        if (graphData.value?.containsKey("smoking_savings") == true || graphData.value?.containsKey("smoking_time_saved") == true) {
            Text("금연 통계", fontSize = 20.sp)
            graphData.value?.get("smoking_savings")?.let { savings ->
                LineChartComponent(
                    label = "절약한 금액 (흡연)",
                    data = savings,
                    xAxisLabelCount = savings.size,
                    yAxisLabel = "원",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            graphData.value?.get("smoking_time_saved")?.let { timeSaved ->
                LineChartComponent(
                    label = "절약한 시간 (흡연)",
                    data = timeSaved,
                    xAxisLabelCount = timeSaved.size,
                    yAxisLabel = "시간",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        // 음주 관련 그래프 (데이터가 있을 때만 제목과 그래프 표시)
        if (graphData.value?.containsKey("drinking_savings") == true || graphData.value?.containsKey("drinking_time_saved") == true) {
            Text("금주 통계", fontSize = 20.sp)
            graphData.value?.get("drinking_savings")?.let { savings ->
                LineChartComponent(
                    label = "절약한 금액 (음주)",
                    data = savings,
                    xAxisLabelCount = savings.size,
                    yAxisLabel = "원",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            graphData.value?.get("drinking_time_saved")?.let { timeSaved ->
                LineChartComponent(
                    label = "절약한 시간 (음주)",
                    data = timeSaved,
                    xAxisLabelCount = timeSaved.size,
                    yAxisLabel = "시간",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        // 게임 관련 그래프 (데이터가 있을 때만 제목과 그래프 표시)
        if (graphData.value?.containsKey("gaming_time_saved") == true) {
            Text("게임 통계", fontSize = 20.sp)
            graphData.value?.get("gaming_time_saved")?.let { timeSaved ->
                LineChartComponent(
                    label = "절약한 시간 (게임)",
                    data = timeSaved,
                    xAxisLabelCount = timeSaved.size,
                    yAxisLabel = "시간",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }
    }
}
