import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.pages.AnimatedRowGraph
import com.heungjun.gaincontrol.pages.CalendarScreen
import com.heungjun.gaincontrol.pages.DetailedPlanInputDialog
import com.heungjun.gaincontrol.pages.GraphListScreen
import com.heungjun.gaincontrol.pages.Plan
import com.heungjun.gaincontrol.pages.getDateKey
import com.heungjun.gaincontrol.pages.sortPlanDetails
import com.heungjun.gaincontrol.pages.updateAccumulatedData
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.viewmodel.HealthGoalsViewModel
import com.heungjun.gaincontrol.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    sharedViewModel: SharedViewModel = viewModel() // 여기서 viewModel() 사용
) {
    val authState = authViewModel.authState.observeAsState()
    val accumulatedData = remember { mutableStateMapOf<String, MutableMap<String, Int>>() }
    val viewModel = HealthGoalsViewModel()

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    // 특정 UID로 데이터 가져오기
    viewModel.fetchHealthGoalsData(uid.toString())

    val today = LocalDate.now()
    val creationDate = authViewModel.getAccountCreationDate()
    val daysElapsed = ChronoUnit.DAYS.between(creationDate, today).toInt() //계정생성부터 지금까지

    val TotalDambe by sharedViewModel.totalDambe.observeAsState(0)
    val TotalSoju by sharedViewModel.totalSoju.observeAsState(0)
    val TotalGame by sharedViewModel.totalGame.observeAsState(0)


    val smokingData = viewModel.smokingData.observeAsState()
    val SperDay = smokingData.value?.perDay ?: 0

    val drinkingData = viewModel.drinkingData.observeAsState()
    val DperWeek = drinkingData.value?.perWeek ?: 0
    val DperSession = drinkingData.value?.amountPerSession ?: 0

    val gamingData = viewModel.gamingData.observeAsState()
    val GperWeek = gamingData.value?.perWeek ?: 0
    val Gperhour = gamingData.value?.hoursPerSession ?: 0

    var SaveMoney_D = ((SperDay * daysElapsed - TotalDambe) * 225).coerceAtLeast(0)
    var SaveMoney_S = ((((DperWeek * DperSession) * (daysElapsed/7)) - TotalSoju) * 5000).coerceAtLeast(0)
    var SaveMoney_G = ((((GperWeek * Gperhour) * (daysElapsed)) - TotalGame) * 1500).coerceAtLeast(0)

    var SaveTime_D = ((SperDay * daysElapsed - TotalDambe) * 7).coerceAtLeast(0)
    var SaveTime_S = ((((DperWeek * DperSession) * (daysElapsed/7)) - TotalSoju) * 4).coerceAtLeast(0)
    var SaveTime_G = (((GperWeek * Gperhour) * (daysElapsed)) - TotalGame).coerceAtLeast(0)

    // 로그인 상태 확인 후 리디렉션
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("statistics") { inclusive = true }
            }
        }
    }

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val plans = remember { mutableStateMapOf<String, MutableList<Plan>>() }
    var showInputDialog by remember { mutableStateOf(false) }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 캘린더 헤더
            Text(
                text = "STATISTICS",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 캘린더 화면
            CalendarScreen(
                selectedDate = selectedDate,
                onDateChange = { newDate ->
                    selectedDate = newDate
                    showInputDialog = true
                },
                plans = plans // 일정 데이터 전달
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 선택된 날짜와 계획 표시
            val selectedDateKey = getDateKey(selectedDate)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${selectedDate.get(Calendar.YEAR)}년 ${selectedDate.get(Calendar.MONTH) + 1}월 ${selectedDate.get(Calendar.DAY_OF_MONTH)}일",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val datePlans = plans[selectedDateKey] ?: emptyList()
                if (datePlans.isEmpty()) {
                    Text(text = "일정이 없습니다.", color = Color.Gray)
                } else {
                    datePlans.forEach { plan ->
                        Text(
                            text = buildString {
                                append("${plan.category} -\n")
                                val sortedDetails = sortPlanDetails(plan.details)
                                append(sortedDetails)
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("아낀 돈", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedRowGraph(label = "담배", value = SaveMoney_D, maxValue = 2000000, barColor = Color.Green)
                    AnimatedRowGraph(label = "술", value = SaveMoney_S, maxValue = 2000000, barColor = Color.Blue)
                    AnimatedRowGraph(label = "게임", value = SaveMoney_G, maxValue = 2000000, barColor = Color.Yellow)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("아낀 시간", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedRowGraph(label = "담배", value = SaveTime_D, maxValue = 200000, barColor = Color.Green)
                    AnimatedRowGraph(label = "술", value = SaveTime_S, maxValue = 200000 , barColor = Color.Blue)
                    AnimatedRowGraph(label = "게임", value = SaveTime_G, maxValue = 200000, barColor = Color.Yellow)
                }}

            GraphListScreen(sharedViewModel = sharedViewModel, onAddClicked = {})
        }

        // 입력 다이얼로그
        if (showInputDialog) {
            DetailedPlanInputDialog(
                onDismiss = { showInputDialog = false },
                onSave = { category, details ->
                    val dateKey = getDateKey(selectedDate)
                    if (plans[dateKey] == null) {
                        plans[dateKey] = mutableListOf()
                    }
                    plans[dateKey]?.add(Plan(category, details))

                    // 누적 데이터 업데이트
                    updateAccumulatedData(accumulatedData, category, details)

                    // Total 값 업데이트
                    when (category) {
                        "담배" -> {
                            val todayDambe = details.lines().find { it.startsWith("avgPacks:") }
                                ?.split(":")
                                ?.get(1)
                                ?.trim()
                                ?.toIntOrNull() ?: 0
                            sharedViewModel.updateTotalDambe(todayDambe)
                        }
                        "술" -> {
                            val todaySoju = details.lines().find { it.startsWith("estimatedAmount:") }
                                ?.split(":")
                                ?.get(1)
                                ?.trim()
                                ?.toIntOrNull() ?: 0
                            sharedViewModel.updateTotalSoju(todaySoju)
                        }
                        "게임" -> {
                            val startTime = details.lines().find { it.startsWith("startTime:") }
                                ?.split(":")
                                ?.get(1)
                                ?.trim()
                                ?.toIntOrNull() ?: 0
                            val endTime = details.lines().find { it.startsWith("endTime:") }
                                ?.split(":")
                                ?.get(1)
                                ?.trim()
                                ?.toIntOrNull() ?: 0
                            val todayGame = if (endTime > startTime) endTime - startTime else 0
                            sharedViewModel.updateTotalGame(todayGame)
                        }
                    }

                    showInputDialog = false
                }
            )
        }
    }
}
