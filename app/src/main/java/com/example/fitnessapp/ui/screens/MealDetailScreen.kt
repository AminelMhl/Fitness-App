package com.example.fitnessapp.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitnessapp.model.SelectedFood
import com.example.fitnessapp.viewmodel.FoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(navController: NavController, mealType: String) {
    val context = LocalContext.current
    val foodViewModel: FoodViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    // Set the current meal type
    foodViewModel.setCurrentMealType(mealType)

    // Get foods for this meal type
    val allFoods by foodViewModel.selectedFoods.collectAsState()
    val mealFoods = allFoods.filter { it.mealType == mealType }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "$mealType Foods", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2196F3)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("food") }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Food",
                            tint = Color(0xFF2196F3)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (mealFoods.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "No foods added to $mealType yet",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("food") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Add Food")
                        }
                    }
                }
            } else {
                // Total nutrition for this meal
                val totalCalories = mealFoods.sumOf { it.foodItem.calories * it.quantity }
                val totalProtein = mealFoods.sumOf { it.foodItem.protein * it.quantity }
                val totalCarbs = mealFoods.sumOf { it.foodItem.carbs * it.quantity }
                val totalFat = mealFoods.sumOf { it.foodItem.fat * it.quantity }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "$mealType Total",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1976D2)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Calories: $totalCalories kcal", color = Color.Red)
                        Text(text = "Protein: $totalProtein g", color = Color(0xFF228B22))
                        Text(text = "Carbs: $totalCarbs g", color = Color(0xFF9C27B0))
                        Text(text = "Fat: $totalFat g", color = Color(0xFF00BCD4))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(mealFoods.size) { index ->
                        val food = mealFoods[index]
                        MealFoodItem(food = food, onRemove = {
                            foodViewModel.removeFood(food.id)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MealFoodItem(food: SelectedFood, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = food.foodItem.imageRes),
                contentDescription = food.foodItem.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.foodItem.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Quantity: ${food.quantity}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Calories: ${food.foodItem.calories * food.quantity} kcal",
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}