package com.heungjun.gaincontrol.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.heungjun.gaincontrol.components.LineChartComponent
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.viewmodel.UserViewModel

@Composable
fun HomePage(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = viewModel()
) {
    val graphData = userViewModel.graphData.observeAsState()

    val scrollState = rememberScrollState()

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 이미지
            val imageUrl =
                "https://img.freepik.com/free-vector/people-celebrating-goal-achievement-flat-hand-drawn_23-2148825627.jpg"
            val painter = rememberAsyncImagePainter(model = imageUrl)

            Image(
                painter = painter,
                contentDescription = "상단 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // 금연 통계
            if (graphData.value?.containsKey("smoking_savings") == true || graphData.value?.containsKey("smoking_time_saved") == true) {
                StatisticsCard(
                    title = "금연 통계",
                    graphData = graphData.value,
                    keys = listOf("smoking_savings", "smoking_time_saved"),
                    labels = listOf("절약한 금액 (흡연)", "절약한 시간 (흡연)"),
                    units = listOf("원", "시간")
                )
            }

            // 금주 통계
            if (graphData.value?.containsKey("drinking_savings") == true || graphData.value?.containsKey("drinking_time_saved") == true) {
                StatisticsCard(
                    title = "금주 통계",
                    graphData = graphData.value,
                    keys = listOf("drinking_savings", "drinking_time_saved"),
                    labels = listOf("절약한 금액 (음주)", "절약한 시간 (음주)"),
                    units = listOf("원", "시간")
                )
            }

            // 게임 통계
            if (graphData.value?.containsKey("gaming_time_saved") == true) {
                StatisticsCard(
                    title = "게임 통계",
                    graphData = graphData.value,
                    keys = listOf("gaming_time_saved"),
                    labels = listOf("절약한 시간 (게임)"),
                    units = listOf("시간")
                )
            }
        }
    }
}

@Composable
fun StatisticsCard(
    title: String,
    graphData: Map<String, List<Double>>?,
    keys: List<String>,
    labels: List<String>,
    units: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDFF6DD) // 연한 초록색
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                color = Color(0xFF3A7D44), // 짙은 초록색
                modifier = Modifier.padding(bottom = 8.dp)
            )

            keys.forEachIndexed { index, key ->
                graphData?.get(key)?.let { data ->
                    LineChartComponent(
                        label = labels[index],
                        data = data,
                        xAxisLabelCount = data.size,
                        yAxisLabel = units.getOrNull(index),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                }
            }
        }
    }
}
