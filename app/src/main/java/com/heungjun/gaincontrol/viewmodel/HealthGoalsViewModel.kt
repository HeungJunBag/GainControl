package com.heungjun.gaincontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


data class SmokingData(
    val goalYears: Int = 0,
    val perDay: Int = 0,
    val startDate: String = "",
    val totalCigarettes: Int = 0
)

data class DrinkingData(
    val amountPerSession: Int = 0,
    val goalYears: Int = 0,
    val perWeek: Int = 0,
    val startDate: String = "",
    val totalDrinks: Int = 0
)

data class GamingData(
    val goalYears: Int = 0,
    val hoursPerSession: Int = 0,
    val perWeek: Int = 0,
    val startDate: String = "",
    val totalGamingHours: Int = 0
)

class HealthGoalsViewModel : ViewModel() {
    private val database: FirebaseDatabase = Firebase.database

    private val _smokingData = MutableLiveData<SmokingData?>()
    val smokingData: LiveData<SmokingData?> = _smokingData

    private val _drinkingData = MutableLiveData<DrinkingData?>()
    val drinkingData: LiveData<DrinkingData?> = _drinkingData

    private val _gamingData = MutableLiveData<GamingData?>()
    val gamingData: LiveData<GamingData?> = _gamingData

    // 특정 UID의 데이터를 가져오는 함수
    fun fetchHealthGoalsData(uid: String) {
        val userRef = database.reference.child("users").child(uid)

        // Smoking 데이터 가져오기
        userRef.child("smoking").get().addOnSuccessListener { snapshot ->
            _smokingData.value = snapshot.toSmokingData()
        }.addOnFailureListener {
            _smokingData.value = null
        }

        // Drinking 데이터 가져오기
        userRef.child("drinking").get().addOnSuccessListener { snapshot ->
            _drinkingData.value = snapshot.toDrinkingData()
        }.addOnFailureListener {
            _drinkingData.value = null
        }

        // Gaming 데이터 가져오기
        userRef.child("gaming").get().addOnSuccessListener { snapshot ->
            _gamingData.value = snapshot.toGamingData()
        }.addOnFailureListener {
            _gamingData.value = null
        }
    }

    private fun DataSnapshot.toSmokingData(): SmokingData? {
        val data = this.getValue(SmokingData::class.java)
        if (data == null) {
            println("Failed to map snapshot to SmokingData: ${this.value}")
        }
        return data
    }

    private fun DataSnapshot.toDrinkingData(): DrinkingData? {
        return this.getValue(DrinkingData::class.java)
    }

    private fun DataSnapshot.toGamingData(): GamingData? {
        return this.getValue(GamingData::class.java)
    }
}
