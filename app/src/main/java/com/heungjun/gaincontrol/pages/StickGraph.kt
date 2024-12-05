import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
                fontSize = 18.sp
            )

            // 금욕 기간 및 목표 기간 정보
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "금욕시간",
                    color = Color.Blue,
                    fontSize = 8.sp
                )
                Text(
                    text = quitYears,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "현재 목표 $goalYears 년",
                        color = Color.Black,
                        fontSize = 8.sp
                    )
                    Text(
                        text = "$goalYears %",
                        color = Color.Black,
                        fontSize = 8.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStickGraph() {
    StatusCard(
        icon = Icons.Filled.SmokingRooms, // Material Design 아이콘
        statusTitle = "흡연",
        quitYears = "10",
        goalYears = "100"
    )
}