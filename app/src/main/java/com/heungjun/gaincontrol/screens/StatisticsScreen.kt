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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heungjun.gaincontrol.commonlayout.GradientBackground
import com.heungjun.gaincontrol.pages.CalendarScreen
import com.heungjun.gaincontrol.pages.DetailedPlanInputDialog
import com.heungjun.gaincontrol.pages.GraphListScreen
import com.heungjun.gaincontrol.pages.Plan
import com.heungjun.gaincontrol.pages.getDateKey
import com.heungjun.gaincontrol.pages.sortPlanDetails
import com.heungjun.gaincontrol.viewmodel.AuthState
import com.heungjun.gaincontrol.viewmodel.AuthViewModel
import com.heungjun.gaincontrol.viewmodel.SharedViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState = authViewModel.authState.observeAsState()
    val sharedViewModel = remember { SharedViewModel() }

    // 로그인 상태 확인 후 리디렉션
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("statistics") { inclusive = true }
            }
        }
    }

    // State variables for calendar and plans
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

            // 그래프 영역 추가
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
                    showInputDialog = false
                }
            )
        }
    }
}