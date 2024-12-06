package com.heungjun.gaincontrol.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusBar(
    name: String,
    toDay: Int,
    goalDate: Int,
    modifier: Modifier = Modifier
) {
    val progress = ((toDay.toFloat() / goalDate) * 100).toInt() // 진행률 계산

    Box(
        modifier = modifier
            .background(Color.LightGray)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Status(
                icon = Icons.Filled.SmokingRooms,
                statusTitle = name,
                quitYears = "$toDay 일",
                goalYears = "$goalDate 일",
                progress = progress
            )
        }
    }
}

@Composable
fun Status(
    icon: ImageVector,
    statusTitle: String,
    quitYears: String,
    goalYears: String,
    progress: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.LightGray)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = statusTitle,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = statusTitle,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Text(
            text = "금욕시간",
            color = Color.Blue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = quitYears,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "현재 목표 $goalYears",
                color = Color.Black,
                fontSize = 12.sp
            )
        }

        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress / 100f)
                    .height(8.dp)
                    .background(Color.Black)
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$progress%",
                color = Color.Black,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStatusBar() {
    StatusBar(
        name = "흡연",
        toDay = 300,
        goalDate = 2190
    )
}
