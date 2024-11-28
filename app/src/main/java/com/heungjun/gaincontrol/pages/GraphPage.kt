package com.heungjun.gaincontrol.pages

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DietRecodeListScreen(onAddClicked: () -> Unit) {

    //나중에 저장된 데이터에서 가져와야함 (원형 그래프 데이터)
    val targetDate = 1500 //목표 일수
    val progress = 300 //현재 일수
    val progress2 = 500 //현재 일수 2

    //나중에 저장된 데이터에서 가져와야함 (막대 그래프 데이터)
    val safeMoney_1 = 10000
    val safeMoney_2 = 10000

    val safeTime_1 = 10000
    val safeTime_2 = 10000

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AnimatedCircularProgressBar(
                        progress,
                        targetDate,
                        modifier = Modifier.size(150.dp),
                        strokeWidth = 10.dp
                    )
                }

                Box(contentAlignment = Alignment.Center) {
                    AnimatedCircularProgressBar(
                        progress2,
                        targetDate,
                        modifier = Modifier.size(150.dp),
                        strokeWidth = 10.dp
                    )
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
    val animatedProgress = remember { Animatable(0f) }

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

        // Draw the background circle
        drawCircle(
            color = backgroundColor,
            radius = radius - strokeWidthPx / 2,
            center = center,
            style = Stroke(width = strokeWidthPx)
        )

        // Draw the progress circle
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
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
fun DietRecodeListScreenFullPreview() {
    DietRecodeListScreen(
        onAddClicked = {}
    )
}
