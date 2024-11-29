package com.heungjun.gaincontrol

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddictionForm(navController: NavController) {
    var habit1 by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "중독 멈춰!!!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "이미지를 눌러주세요",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

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

        OutlinedTextField(
            value = habit1,
            onValueChange = { habit1 = it },
            label = { Text("줄이고 싶은 습관이 있으신가요?") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("home") }, // Navigate to HomePage
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "다음", fontSize = 16.sp)
        }
    }

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
    AddictionForm(navController = rememberNavController())
}
