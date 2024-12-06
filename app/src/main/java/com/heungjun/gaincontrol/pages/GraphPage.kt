package com.heungjun.gaincontrol.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.runtime.remember as remember1
import com.heungjun.gaincontrol.viewmodel.HealthGoalsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraphListScreen(onAddClicked: () -> Unit) {
    val viewModel = HealthGoalsViewModel()
    val viewModel2 = HealthGoalsViewModel()

// 특정 UID로 데이터 가져오기
    viewModel.fetchHealthGoalsData("MfNiz9oIWuZKSRb3Gto1p5GXcjd2")
    viewModel2.fetchHealthGoalsData("XyK44WoetHN3c5oV7wanK7zGGuw2")

    val smokingData = viewModel.smokingData.observeAsState()
    val drinkingData = viewModel2.drinkingData.observeAsState()
    val gamingData = viewModel.gamingData.observeAsState()


//  나중에 저장된 데이터에서 가져와야함 (막대 그래프 데이터)
    val safeMoney_1 = 82
    val safeMoney_2 = 63

    val Moneyaim = 300 //목표

    val safeTime_1 = 21
    val safeTime_2 = 42

    val Timeaim = 200 //목표

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //막대그래프 삽입
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                AnimatedRowGraph(label = "게임", value = safeMoney_1, maxValue = Moneyaim, barColor = Color.Yellow)
                AnimatedRowGraph(label = "담배", value = safeMoney_2, maxValue = Moneyaim, barColor = Color.Green)
            }
            Text(
                text = "아낀 돈: " + (safeMoney_1 + safeMoney_2).toString(),
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.padding(20.dp))

            // safeTime 가로 막대그래프
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                AnimatedRowGraph(label = "게임", value = safeTime_1, maxValue = Timeaim, barColor = Color.Yellow)
                AnimatedRowGraph(label = "담배", value = safeTime_2, maxValue = Timeaim, barColor = Color.Green)
            }
            Text(
                text = "아낀 시간: " + (safeTime_1 + safeTime_2).toString(),
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            Spacer(modifier = Modifier.padding(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    smokingData.value?.let { data ->
                        val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        val today = LocalDate.now()
                        val daysElapsed = ChronoUnit.DAYS.between(startDate, today).toInt()
                        val goalDate = data.goalYears * 365
                        Box(contentAlignment = Alignment.Center) {
                            AnimatedCircularProgressBar(
                                progress = daysElapsed,
                                targetDate = goalDate,
                                modifier = Modifier.size(150.dp),
                                strokeWidth = 10.dp
                            )
                        }

                        Spacer(modifier = Modifier.padding(5.dp))

                        StatusCard(
                            icon = Icons.Filled.SmokingRooms, // Material Design 아이콘
                            statusTitle = "흡연",
                            quitYears = "$daysElapsed 일",
                            goalYears = "$goalDate 일"
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    drinkingData.value?.let { data ->
                        val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        val today = LocalDate.now()
                        val daysElapsed = ChronoUnit.DAYS.between(startDate, today).toInt()
                        val goalDate = data.goalYears * 365

                        Box(contentAlignment = Alignment.Center) {
                            AnimatedCircularProgressBar(
                                progress = daysElapsed,
                                targetDate = data.goalYears * 365,
                                modifier = Modifier.size(150.dp),
                                strokeWidth = 10.dp
                            )
                        }

                        Spacer(modifier = Modifier.padding(5.dp))

                        StatusCard(
                            icon = Icons.Filled.VideogameAsset, // Material Design 아이콘
                            statusTitle = "게임",
                            quitYears = "$daysElapsed 일",
                            goalYears = "$goalDate 일"
                        )
                    }

                }
            }
        }

    }
}
@Composable
fun AnimatedCircularProgressBar(
    progress: Int,
    targetDate: Int,
    modifier: Modifier,
    strokeWidth: Dp
) {
    val animatedProgress = remember1 { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = progress.toFloat() / targetDate,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    CircularProgressBar(
        progress = animatedProgress.value,
        modifier = modifier,
        strokeWidth = strokeWidth
    )

    Text(
        text = "${(animatedProgress.value * 100).toInt()}%",
        color = Color.Black,
        fontSize = 30.sp,
        fontWeight = FontWeight.Light
    )
}

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier,
    strokeWidth: Dp,
    color: Color = Color(0xFF800080),
    backgroundColor : Color = Color.LightGray
) {

    Canvas(modifier = modifier){

        val size = size.minDimension
        val radius = size / 2f
        val center = Offset(size / 2f, size / 2f)
        val startAngle = -90f
        val strokeWidthPx = strokeWidth.toPx()

        //백그라운드
        drawCircle(
            color = backgroundColor,
            radius = radius - strokeWidthPx / 2,
            center = center,
            style = Stroke(width = strokeWidthPx)
        )

        //진행도
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = progress * 360,
            useCenter = false,
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
            size = Size(size - strokeWidthPx, size - strokeWidthPx),
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
        )

    }

}
@Composable
fun AnimatedRowGraph(label: String, value: Int, maxValue: Int, barColor: Color) {

    val animatedValue = remember1 { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = value.toFloat() / maxValue,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 레이블
        Text(
            text = label,
            color = Color.Black,
            fontSize = 13.sp,
            modifier = Modifier.width(50.dp)
        )

        // 백그라운드 막대
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            //막대
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(animatedValue.value)
                    .background(barColor)
            )
            Text(
                text = "$value",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }
    }
}

@Composable
fun StatusCard (
    icon: ImageVector,
    statusTitle: String,
    quitYears: String,
    goalYears: String,
    backgroundColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(16.dp)
            .size(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // 아이콘
            Icon(
                imageVector = icon,
                contentDescription = statusTitle,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )

            // 제목
            Text(
                text = statusTitle,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            // 금욕 기간 및 목표 기간 정보
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "금욕시간",
                        color = Color.Blue,
                        fontSize = 12.sp
                    )
                    Text(
                        text = quitYears,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "목표시간",
                        color = Color.Blue,
                        fontSize = 12.sp
                    )
                    Text(
                        text = goalYears,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
fun DietRecodeListScreenFullPreview() {
    GraphListScreen(
        onAddClicked = {}
    )
}
