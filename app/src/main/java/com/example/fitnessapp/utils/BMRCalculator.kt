package com.example.fitnessapp.utils

fun calculateBMR(weightKg: Double, heightCm: Double, age: Int, isMale: Boolean): Double {
    return if (isMale)
        10 * weightKg + 6.25 * heightCm - 5 * age + 5
    else
        10 * weightKg + 6.25 * heightCm - 5 * age - 161
}
