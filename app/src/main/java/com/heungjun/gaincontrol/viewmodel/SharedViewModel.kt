package com.heungjun.gaincontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _totalDambe = MutableLiveData(0)
    val totalDambe: LiveData<Int> = _totalDambe

    private val _totalSoju = MutableLiveData(0)
    val totalSoju: LiveData<Int> = _totalSoju

    private val _totalGame = MutableLiveData(0)
    val totalGame: LiveData<Int> = _totalGame

    fun updateTotalDambe(value: Int) {
        _totalDambe.value = (_totalDambe.value ?: 0) + value
    }

    fun updateTotalSoju(value: Int) {
        _totalSoju.value = (_totalSoju.value ?: 0) + value
    }

    fun updateTotalGame(value: Int) {
        _totalGame.value = (_totalGame.value ?: 0) + value
    }
}
