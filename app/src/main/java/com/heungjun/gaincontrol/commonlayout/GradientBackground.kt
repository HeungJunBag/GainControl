package com.heungjun.gaincontrol.commonlayout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // 기본 배경
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, size.height * 0.3f)
                    cubicTo(
                        size.width * 0.3f, size.height * 0.4f,
                        size.width * 0.7f, size.height * 0.1f,
                        size.width, size.height * 0.3f
                    )
                    lineTo(size.width, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = Color(0xFF81C784) // 연한 초록색
            )
        }
        content()
    }
}