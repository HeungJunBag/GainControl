package com.heungjun.gaincontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.heungjun.gaincontrol.models.*

class UserViewModel : ViewModel() {
    private val _userHabits = MutableLiveData<UserHabits>()
    val userHabits: LiveData<UserHabits> = _userHabits

    private val _graphData = MutableLiveData<Map<String, List<Double>>>()
    val graphData: LiveData<Map<String, List<Double>>> = _graphData

    private val _isNewUser = MutableLiveData<Boolean>()
    val isNewUser: LiveData<Boolean> = _isNewUser

    init {
        checkUserData()
    }

    // 사용자 데이터가 존재하는지 확인
    fun checkUserData() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser ?: run {
            _userHabits.value = UserHabits() // 초기화
            _graphData.value = emptyMap() // 초기화
            _isNewUser.value = true // 신규 사용자로 설정
            return
        }

        val uid = currentUser.uid
        val database = FirebaseDatabase.getInstance().getReference("users")

        database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // 사용자 데이터가 존재하면 기존 데이터 로드
                    val smoking = snapshot.child("smoking").getValue(SmokingData::class.java)
                    val drinking = snapshot.child("drinking").getValue(DrinkingData::class.java)
                    val gaming = snapshot.child("gaming").getValue(GamingData::class.java)

                    _userHabits.value = UserHabits(
                        smoking = smoking,
                        drinking = drinking,
                        gaming = gaming
                    )
                    _isNewUser.value = false // 기존 사용자
                    generateGraphData()
                } else {
                    // 사용자 데이터가 없으면 신규 사용자로 설정
                    _userHabits.value = UserHabits() // 초기화
                    _graphData.value = emptyMap() // 초기화
                    _isNewUser.value = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _userHabits.value = UserHabits() // 초기화
                _graphData.value = emptyMap() // 초기화
                _isNewUser.value = true
            }
        })
    }

    // 그래프 데이터 생성
    private fun generateGraphData() {
        val habits = _userHabits.value ?: return
        val graphDataMap = mutableMapOf<String, List<Double>>()

        // 흡연 데이터
        habits.calculateSmokingSavingsPerYear()?.let {
            graphDataMap["smoking_savings"] = it.map { value -> value.toDouble() }
        }
        habits.calculateSmokingTimeSavedPerYear()?.let {
            graphDataMap["smoking_time_saved"] = it
        }

        // 음주 데이터
        habits.calculateDrinkingSavingsPerYear()?.let {
            graphDataMap["drinking_savings"] = it.map { value -> value.toDouble() }
        }
        habits.calculateDrinkingTimeSavedPerYear()?.let {
            graphDataMap["drinking_time_saved"] = it
        }

        // 게임 데이터 (돈 절약 없음, 시간 절약만 존재)
        habits.calculateGamingTimeSavedPerYear()?.let {
            graphDataMap["gaming_time_saved"] = it
        }

        _graphData.value = graphDataMap
    }
}
