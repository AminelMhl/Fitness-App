package com.example.fitnessapp.model

import java.util.UUID

data class SelectedFood(
    val id: String = UUID.randomUUID().toString(),
    val foodItem: FoodItem,
    val quantity: Int = 1,
    val mealType: String = "Breakfast",
    val timestamp: Long = System.currentTimeMillis()
)