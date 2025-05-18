package com.example.fitnessapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.model.SelectedFood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.fitnessapp.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("fitness_prefs", Application.MODE_PRIVATE)

    private val _user = MutableStateFlow(loadUserData())
    val user: StateFlow<User> = _user

    private fun loadUserData(): User {
        return User(
            age = sharedPreferences.getInt("age", 0),
            weight = sharedPreferences.getFloat("weight", 0f),
            height = sharedPreferences.getFloat("height", 0f),
            isMale = sharedPreferences.getBoolean("isMale", true),
            goal = sharedPreferences.getString("goal", "Maintain") ?: "Maintain",
            caloriesBudget = sharedPreferences.getFloat("caloriesBudget", 0f),
            caloriesConsumed = sharedPreferences.getFloat("caloriesConsumed", 0f),
            proteinGrams = sharedPreferences.getFloat("proteinGrams", 0f),
            carbsGrams = sharedPreferences.getFloat("carbsGrams", 0f),
            fatGrams = sharedPreferences.getFloat("fatGrams", 0f)
        )
    }

    fun refreshNutritionData() {
        // This forces a reload of saved data
        _user.value = loadUserData()
    }

    fun saveUserData(age: Int, weight: Float, height: Float, isMale: Boolean, goal: String) {
        viewModelScope.launch {
            // Calculate BMR and adjust for goal
            val bmr = calculateBMR(weight, height, age, isMale)
            val caloriesBudget = when (goal) {
                "Gain" -> bmr + 400
                "Lose" -> bmr - 200
                else -> bmr // "Maintain"
            }

            // Update in-memory state
            _user.value = _user.value.copy(
                age = age,
                weight = weight,
                height = height,
                isMale = isMale,
                goal = goal,
                caloriesBudget = caloriesBudget
            )

            // Save to SharedPreferences
            sharedPreferences.edit().apply {
                putInt("age", age)
                putFloat("weight", weight)
                putFloat("height", height)
                putBoolean("isMale", isMale)
                putString("goal", goal)
                putFloat("caloriesBudget", caloriesBudget)
                putFloat("caloriesConsumed", _user.value.caloriesConsumed)
                putFloat("proteinGrams", _user.value.proteinGrams)
                putFloat("carbsGrams", _user.value.carbsGrams)
                putFloat("fatGrams", _user.value.fatGrams)
                apply()
            }
        }
    }

    // Existing methods...
    fun calculateBMR(weight: Float, height: Float, age: Int, isMale: Boolean): Float {
        return com.example.fitnessapp.utils.calculateBMR(
            weightKg = weight.toDouble(),
            heightCm = height.toDouble(),
            age = age,
            isMale = isMale
        ).toFloat()
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

    // Add method to update nutrition data
    fun updateNutritionData(calories: Float, protein: Float, carbs: Float, fat: Float) {
        viewModelScope.launch {
            _user.value = _user.value.copy(
                caloriesConsumed = calories,
                proteinGrams = protein,
                carbsGrams = carbs,
                fatGrams = fat
            )

            // Save the nutrition data to SharedPreferences
            sharedPreferences.edit().apply {
                putFloat("caloriesConsumed", calories)
                putFloat("proteinGrams", protein)
                putFloat("carbsGrams", carbs)
                putFloat("fatGrams", fat)
                apply()
            }
        }
    }

    companion object {
        @Volatile
        private var instance: HomeViewModel? = null

        fun getInstance(application: Application): HomeViewModel {
            return instance ?: synchronized(this) {
                instance ?: HomeViewModel(application).also { instance = it }
            }
        }
    }
}

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("food_prefs", Application.MODE_PRIVATE)
    private val gson = Gson()

    private val _selectedFoods = MutableStateFlow<List<SelectedFood>>(loadSelectedFoods())
    val selectedFoods: StateFlow<List<SelectedFood>> = _selectedFoods

    // Current meal type state flow
    private val _currentMealType = MutableStateFlow("Breakfast")
    val currentMealType: StateFlow<String> = _currentMealType

    // Filtered food lists by meal type
    val breakfastFoods = selectedFoods.map { foods -> foods.filter { it.mealType == "Breakfast" } }
    val lunchFoods = selectedFoods.map { foods -> foods.filter { it.mealType == "Lunch" } }
    val dinnerFoods = selectedFoods.map { foods -> foods.filter { it.mealType == "Dinner" } }

    // Load foods from SharedPreferences
    private fun loadSelectedFoods(): List<SelectedFood> {
        val foodsJson = sharedPreferences.getString("selected_foods", null) ?: return emptyList()
        try {
            val type = object : com.google.gson.reflect.TypeToken<List<SelectedFood>>() {}.type
            return gson.fromJson(foodsJson, type)
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    // Save foods to SharedPreferences
    private fun saveSelectedFoods() {
        val foodsJson = gson.toJson(_selectedFoods.value)
        sharedPreferences.edit().putString("selected_foods", foodsJson).apply()
    }

    fun setCurrentMealType(mealType: String) {
        _currentMealType.value = mealType
    }

    // Modified to convert UI FoodItem to model FoodItem
    fun addFood(foodItem: com.example.fitnessapp.ui.screens.FoodItem, quantity: Int = 1, mealType: String = _currentMealType.value) {
        viewModelScope.launch {
            // Convert UI FoodItem to model FoodItem
            val modelFoodItem = com.example.fitnessapp.model.FoodItem(
                name = foodItem.name,
                imageRes = foodItem.imageRes,
                calories = foodItem.calories,
                protein = foodItem.protein,
                carbs = foodItem.carbs,
                fat = foodItem.fat
            )

            val selectedFood = SelectedFood(
                foodItem = modelFoodItem,
                quantity = quantity,
                mealType = mealType
            )

            _selectedFoods.value = _selectedFoods.value + selectedFood
            saveSelectedFoods()
            updateNutritionTotals()
        }
    }

    fun updateFoodQuantity(id: String, quantity: Int) {
        if (quantity <= 0) {
            removeFood(id)
            return
        }

        viewModelScope.launch {
            _selectedFoods.value = _selectedFoods.value.map {
                if (it.id == id) it.copy(quantity = quantity) else it
            }
            saveSelectedFoods()
            updateNutritionTotals()
        }
    }

    fun removeFood(id: String) {
        viewModelScope.launch {
            _selectedFoods.value = _selectedFoods.value.filter { it.id != id }
            saveSelectedFoods()
            updateNutritionTotals()
        }
    }

    fun clearAllFoods() {
        viewModelScope.launch {
            _selectedFoods.value = emptyList()
            saveSelectedFoods()
            updateNutritionTotals()
        }
    }

    fun refreshFoodData() {
        // Reload selected foods from storage
        _selectedFoods.value = loadSelectedFoods()
        // Update nutrition totals based on current data
        updateNutritionTotals()
    }

    private fun updateNutritionTotals() {
        val totalCalories = _selectedFoods.value.sumOf {
            it.foodItem.calories * it.quantity
        }.toFloat()

        val totalProtein = _selectedFoods.value.sumOf {
            it.foodItem.protein * it.quantity
        }.toFloat()

        val totalCarbs = _selectedFoods.value.sumOf {
            it.foodItem.carbs * it.quantity
        }.toFloat()

        val totalFat = _selectedFoods.value.sumOf {
            it.foodItem.fat * it.quantity
        }.toFloat()

        // Get HomeViewModel and update nutrition - improve instance retrieval
        val homeViewModel = HomeViewModel.getInstance(getApplication())
        homeViewModel.updateNutritionData(
            totalCalories,
            totalProtein,
            totalCarbs,
            totalFat
        )
    }
}

