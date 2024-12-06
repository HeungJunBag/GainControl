package com.heungjun.gaincontrol.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.heungjun.gaincontrol.models.UserHabits
import androidx.compose.runtime.remember as remember1
import com.heungjun.gaincontrol.viewmodel.HealthGoalsViewModel
import com.heungjun.gaincontrol.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraphListScreen(sharedViewModel: SharedViewModel, onAddClicked: () -> Unit) {
    val totalDambe by sharedViewModel.totalDambe.observeAsState(0)
    val totalSoju by sharedViewModel.totalSoju.observeAsState(0)
    val totalGame by sharedViewModel.totalGame.observeAsState(0)

    val viewModel = HealthGoalsViewModel()

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    // 특정 UID로 데이터 가져오기
    viewModel.fetchHealthGoalsData(uid.toString())

    val smokingData = viewModel.smokingData.observeAsState()
    val drinkingData = viewModel.drinkingData.observeAsState()
    val gamingData = viewModel.gamingData.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("누적 데이터 요약", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedRowGraph(label = "담배", value = totalDambe, maxValue = 100, barColor = Color.Green)
                AnimatedRowGraph(label = "술", value = totalSoju, maxValue = 100, barColor = Color.Blue)
                AnimatedRowGraph(label = "게임", value = totalGame, maxValue = 100, barColor = Color.Yellow)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 원형 그래프와 상태 카드 3개를 가로로 배치
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 흡연 데이터
                smokingData.value?.let { data ->
                    val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val today = LocalDate.now()
                    val daysElapsed = ChronoUnit.DAYS.between(startDate, today).toInt()
                    val goalDate = data.goalYears * 365

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            AnimatedCircularProgressBar(
                                progress = daysElapsed,
                                targetDate = goalDate,
                                modifier = Modifier.size(120.dp),
                                strokeWidth = 8.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        StatusCard(
                            icon = Icons.Filled.SmokingRooms,
                            statusTitle = "흡연",
                            quitYears = "$daysElapsed 일",
                            goalYears = "$goalDate 일"
                        )
                    }
                }

                // 음주 데이터
                drinkingData.value?.let { data ->
                    val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val today = LocalDate.now()
                    val daysElapsed = ChronoUnit.DAYS.between(startDate, today).toInt()
                    val goalDate = data.goalYears * 365

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            AnimatedCircularProgressBar(
                                progress = daysElapsed,
                                targetDate = goalDate,
                                modifier = Modifier.size(120.dp),
                                strokeWidth = 8.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        StatusCard(
                            icon = Icons.Filled.VideogameAsset,
                            statusTitle = "음주",
                            quitYears = "$daysElapsed 일",
                            goalYears = "$goalDate 일"
                        )
                    }
                }

                // 게임 데이터
                gamingData.value?.let { data ->
                    val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val today = LocalDate.now()
                    val daysElapsed = ChronoUnit.DAYS.between(startDate, today).toInt()
                    val goalDate = data.goalYears * 365

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            AnimatedCircularProgressBar(
                                progress = daysElapsed,
                                targetDate = goalDate,
                                modifier = Modifier.size(120.dp),
                                strokeWidth = 8.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        StatusCard(
                            icon = Icons.Filled.VideogameAsset,
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

    Box(
        contentAlignment = Alignment.Center, // 텍스트를 기본적으로 중앙에 배치
        modifier = modifier
    ) {
        CircularProgressBar(
            progress = animatedProgress.value,
            modifier = Modifier.matchParentSize(),
            strokeWidth = strokeWidth
        )

        // 텍스트를 약간 위로 이동
        Text(
            text = "${(animatedProgress.value * 100).toInt()}%", // 진행 퍼센트
            color = Color.Black,
            fontSize = 19.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.offset(y = (-14).dp) // 위로 6dp 이동
        )
    }
}


@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier,
    strokeWidth: Dp,
    color: Color = Color(0xFF800080),
    backgroundColor: Color = Color.LightGray
) {
    Canvas(modifier = modifier) {
        val size = size.minDimension
        val radius = size / 2f
        val center = Offset(size / 2f, size / 2f)
        val startAngle = -90f
        val strokeWidthPx = strokeWidth.toPx()

        // 백그라운드
        drawCircle(
            color = backgroundColor,
            radius = radius - strokeWidthPx / 2,
            center = center,
            style = Stroke(width = strokeWidthPx)
        )

        // 진행도
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
fun StatusCard(
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
        }

        // 값 표시
        Text(
            text = "$value",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//fun DietRecodeListScreenFullPreview() {
//    GraphListScreen(
//        onAddClicked = {}
//    )
//}
