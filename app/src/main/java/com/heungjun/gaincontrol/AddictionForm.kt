package com.heungjun.gaincontrol

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddictionForm() {
    // State variable for user input
    var habit1 by remember { mutableStateOf("") }

    // State variable to control dialog visibility
    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center vertically
    ) {
        Text(
            text = "중독 멈춰!!!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Instruction text above the image
        Text(
            text = "이미지를 눌러주세요",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Clickable Image for Category Selection
        IconButton(onClick = { isDialogOpen = true }) {
            Image(
                painter = painterResource(id = R.drawable.no_sign),
                contentDescription = "No Addiction Sign",
                modifier = Modifier
                    .height(80.dp)
                    .padding(vertical = 1.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Habit Input Field with selected category in field
        OutlinedTextField(
            value = habit1,
            onValueChange = { habit1 = it },
            label = { Text("줄이고 싶은 습관이 있으신가요?") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add Information Button
        Button(
            onClick = { /* Action to save info */ },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "다음", fontSize = 16.sp)
        }
    }

    // Full-screen category selection dialog
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = {
                Text(
                    text = "카테고리를 선택하세요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    CategoryItem("흡연", R.drawable.baseline_smoking_rooms_24) { habit1 = "흡연"; isDialogOpen = false }
                    CategoryItem("음주", R.drawable.baseline_local_drink_24) { habit1 = "음주"; isDialogOpen = false }
                    CategoryItem("게임", R.drawable.baseline_videogame_asset_24) { habit1 = "게임"; isDialogOpen = false }
                    // Add other categories here using CategoryItem composable...
                }
            },
            confirmButton = {
                Text(
                    text = "취소",
                    modifier = Modifier
                        .clickable { isDialogOpen = false }
                        .padding(8.dp),
                    color = Color.Red
                )
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}

@Composable
fun CategoryItem(name: String, iconResId: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = name,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(text = name, fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddictionForm() {
    AddictionForm()
}
