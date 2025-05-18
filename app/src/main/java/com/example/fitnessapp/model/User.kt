package com.example.fitnessapp.model

data class User(
    val age: Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f,
    val isMale: Boolean = true,
    val goal: String = "Maintain",
    val caloriesBudget: Float = 0f,
    val caloriesConsumed: Float = 0f,
    val proteinGrams: Float = 0f,
    val carbsGrams: Float = 0f,
    val fatGrams: Float = 0f
)

