package com.heungjun.gaincontrol.models

import com.heungjun.gaincontrol.viewmodel.DrinkingData
import com.heungjun.gaincontrol.viewmodel.GamingData
import com.heungjun.gaincontrol.viewmodel.SmokingData

data class UserHabits(
    val smoking: SmokingData? = null,
    val drinking: DrinkingData? = null,
    val gaming: GamingData? = null
) {
    // 흡연 계산 로직
    fun calculateSmokingSavingsPerYear(costPerCigarette: Int = 150): List<Int>? {
        return smoking?.let {
            val savings = mutableListOf<Int>()
            for (year in 1..it.goalYears) {
                val yearlySavings = it.perDay * 365 * costPerCigarette
                savings.add(yearlySavings * year)
            }
            savings
        }
    }

    fun calculateSmokingTimeSavedPerYear(timePerCigarette: Int = 5): List<Double>? {
        return smoking?.let {
            val timeSaved = mutableListOf<Double>()
            for (year in 1..it.goalYears) {
                val yearlyTimeSaved = it.perDay * 365 * timePerCigarette / 60.0
                timeSaved.add(yearlyTimeSaved * year)
            }
            timeSaved
        }
    }

    // 음주 계산 로직
    fun calculateDrinkingSavingsPerYear(costPerDrink: Int = 5000): List<Int>? {
        return drinking?.let {
            val savings = mutableListOf<Int>()
            for (year in 1..it.goalYears) {
                val dailyAverage = it.amountPerSession * it.perWeek / 7.0
                val yearlySavings = (dailyAverage * 365 * costPerDrink).toInt()
                savings.add(yearlySavings * year)
            }
            savings
        }
    }

    fun calculateDrinkingTimeSavedPerYear(timePerDrink: Int = 30): List<Double>? {
        return drinking?.let {
            val timeSaved = mutableListOf<Double>()
            for (year in 1..it.goalYears) {
                val dailyAverage = it.amountPerSession * it.perWeek / 7.0
                val yearlyTimeSaved = dailyAverage * 365 * timePerDrink / 60.0
                timeSaved.add(yearlyTimeSaved * year)
            }
            timeSaved
        }
    }

    // 게임 계산 로직
    fun calculateGamingTimeSavedPerYear(): List<Double>? {
        return gaming?.let {
            val timeSaved = mutableListOf<Double>()
            for (year in 1..it.goalYears) {
                val dailyAverage = it.hoursPerSession * it.perWeek / 7.0
                val yearlyTimeSaved = dailyAverage * 365
                timeSaved.add(yearlyTimeSaved * year)
            }
            timeSaved
        }
    }
}

data class SmokingData(
    val goalYears: Int = 0,
    val perDay: Int = 0
)

data class DrinkingData(
    val amountPerSession: Int = 0,
    val goalYears: Int = 0,
    val perWeek: Int = 0
)

data class GamingData(
    val goalYears: Int = 0,
    val hoursPerSession: Int = 0,
    val perWeek: Int = 0
)
