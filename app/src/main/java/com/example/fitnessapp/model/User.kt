package com.example.fitnessapp.model

data class User(
    var weight: Float = 70f, // in kg
    var height: Float = 175f, // in cm
    var age: Int = 25,
    var isMale: Boolean = true,
    var caloriesConsumed: Float = 1200f
)
