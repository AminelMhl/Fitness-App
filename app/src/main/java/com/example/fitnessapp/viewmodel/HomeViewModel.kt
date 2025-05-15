package com.example.fitnessapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.fitnessapp.model.User
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    fun calculateBMR(weight: Float, height: Float, age: Int, isMale: Boolean): Float {
        return if (isMale) {
            10 * weight + 6.25f * height - 5 * age + 5
        } else {
            10 * weight + 6.25f * height - 5 * age - 161
        }
    }

    fun getProgress(consumed: Float, bmr: Float): Float {
        return consumed / bmr
    }

    fun editUserData() {
        // Navigation to edit screen or update logic here
    }

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    // Optional for Preview
    fun setUserDummyData() {
        _user.value = User(weight = 75f, height = 180f, age = 28, isMale = true, caloriesConsumed = 1500f)
    }
}
