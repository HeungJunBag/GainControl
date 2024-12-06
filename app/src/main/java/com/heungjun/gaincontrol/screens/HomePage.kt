package com.heungjun.gaincontrol.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heungjun.gaincontrol.R
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Page", fontSize = 24.sp)

        // 흡연 관련 그래프
        Text("Smoking Graphs", fontSize = 20.sp)
        graphData.value?.get("smoking_savings")?.let { savings ->
            LineChartComponent(
                label = "Money Saved (Smoking)",
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
                label = "Time Saved (Smoking)",
                data = timeSaved,
                xAxisLabelCount = timeSaved.size,
                yAxisLabel = "시간",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        // 음주 관련 그래프
        Text("Drinking Graphs", fontSize = 20.sp)
        graphData.value?.get("drinking_savings")?.let { savings ->
            LineChartComponent(
                label = "Money Saved (Drinking)",
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
                label = "Time Saved (Drinking)",
                data = timeSaved,
                xAxisLabelCount = timeSaved.size,
                yAxisLabel = "시간",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        // 게임 관련 그래프
        Text("Gaming Graphs", fontSize = 20.sp)
        graphData.value?.get("gaming_time_saved")?.let { timeSaved ->
            LineChartComponent(
                label = "Time Saved (Gaming)",
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
