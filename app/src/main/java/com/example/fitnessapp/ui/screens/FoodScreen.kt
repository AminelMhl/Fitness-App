package com.example.fitnessapp.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.fitnessapp.R
import com.example.fitnessapp.viewmodel.FoodViewModel

data class FoodCategory(val name: String, val imageRes: Int)

data class FoodItem(
    val name: String,
    val imageRes: Int,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)

@Composable
fun FoodScreen(navController: NavController) {
    val categories = listOf(
        FoodCategory("Drinks", R.drawable.drink),
        FoodCategory("Vegetables", R.drawable.vegetables),
        FoodCategory("Carbs", R.drawable.carb),
        FoodCategory("Fruits", R.drawable.fruit),
        FoodCategory("Protein", R.drawable.protein1),
        FoodCategory("Snack", R.drawable.snack),
        FoodCategory("post_Workout", R.drawable.postworkout),
        FoodCategory("Pre-Workout", R.drawable.preworkout),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(categories) { category ->
            CategoryCard(name = category.name, imageRes = category.imageRes) {
                navController.navigate("category/${category.name}")
            }
        }
    }
}

@Composable
fun CategoryCard(name: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),


        ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(categoryName: String, navController: NavController) {
    val context = LocalContext.current
    val foodViewModel: FoodViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    val currentMealType by foodViewModel.currentMealType.collectAsState()

    // This will force the FoodViewModel to refresh its data when the screen is displayed
    LaunchedEffect(Unit) {
        foodViewModel.refreshFoodData()
    }

    val foodItemsByCategory = mapOf(
        "Drinks" to listOf(
            FoodItem("Water", R.drawable.wat, 0, 0.0, 0.0, 0.0),
            FoodItem("Green Tea", R.drawable.green, 2, 0.0, 0.0, 0.0),
            FoodItem("Protein Shake", R.drawable.shake, 150, 25.0, 5.0, 2.0)
        ),
        "Vegetables" to listOf(
            FoodItem("Broccoli", R.drawable.broc, 55, 3.7, 11.2, 0.6),
            FoodItem("Spinach", R.drawable.spin, 23, 2.9, 3.6, 0.4),
            FoodItem("Carrots", R.drawable.carrot, 41, 0.9, 10.0, 0.2)
        ),
        "Carbs" to listOf(
            FoodItem("Brown Rice", R.drawable.rice, 216, 5.0, 44.8, 1.8),
            FoodItem("Pasta", R.drawable.grilled_veggie_pasta_salad, 219, 5.0, 46.1, 2.0),
            FoodItem("Oats", R.drawable.oats, 150, 5.0, 27.0, 3.0),
            FoodItem("Sweet Potato", R.drawable.potato, 86, 1.6, 20.1, 0.1)
        ),
        "Fruits" to listOf(
            FoodItem("Banana", R.drawable.ban, 89, 1.1, 23.0, 0.3),
            FoodItem("Apple", R.drawable.apple, 52, 0.3, 14.0, 0.2),
            FoodItem("Berries", R.drawable.berries1, 57, 0.7, 14.5, 0.3)
        ),
        "Protein" to listOf(
            FoodItem("Chicken Breast", R.drawable.chic, 165, 31.0, 0.0, 3.6),
            FoodItem("Tofu", R.drawable.tofu, 76, 8.0, 1.9, 4.8),
            FoodItem("Eggs", R.drawable.eggs, 155, 13.0, 1.1, 11.0)
        ),
        "Snack" to listOf(
            FoodItem("Nuts", R.drawable.snack, 607, 20.0, 21.0, 54.0),
            FoodItem("Granola Bar", R.drawable.gran, 120, 3.0, 18.0, 4.0),
            FoodItem("Greek Yogurt", R.drawable.oats, 59, 10.0, 3.6, 0.4),
            FoodItem("Banana", R.drawable.ban, 89, 1.1, 23.0, 0.3),
            FoodItem("Apple", R.drawable.apple, 52, 0.3, 14.0, 0.2),
            FoodItem("Berries", R.drawable.berries1, 57, 0.7, 14.5, 0.3)
        ),
        "Pre-Workout" to listOf(
            FoodItem("Banana", R.drawable.ban, 89, 1.1, 23.0, 0.3),
            FoodItem("Protein Shake", R.drawable.shake, 150, 25.0, 5.0, 2.0),
            FoodItem("Oats", R.drawable.oats, 150, 5.0, 27.0, 3.0)
        ),
        "post_Workout" to listOf(
            FoodItem("Protein Shake", R.drawable.shake, 150, 25.0, 5.0, 2.0),
            FoodItem("Chicken & Rice", R.drawable.chicken, 400, 40.0, 50.0, 5.0)
        )
    )

    val foodItems = foodItemsByCategory[categoryName] ?: emptyList()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = categoryName, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
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
        ) {
            // Meal type selector
            MealTypeSelector(
                currentMealType = currentMealType,
                onMealTypeSelected = { foodViewModel.setCurrentMealType(it) }
            )

            Text(
                text = "Adding to: $currentMealType",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )

            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(foodItems.size) { index ->
                    val item = foodItems[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color(0xFF003366)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Calories: ${item.calories} kcal", color = Color.Red, fontSize = 16.sp)
                                Text(text = "Protein: ${item.protein} g", color = Color(0xFF228B22), fontSize = 16.sp)
                                Text(text = "Carbs: ${item.carbs} g", color = Color(0xFF9C27B0), fontSize = 16.sp)
                                Text(text = "Fat: ${item.fat} g", color =  Color(0xFF00BCD4), fontSize = 16.sp)
                            }

                            IconButton(
                                onClick = {
                                    foodViewModel.addFood(item, 1, currentMealType)
                                    foodViewModel.refreshFoodData()
                                    navController.navigate("meal/$currentMealType") {
                                        // This is the key fix - it replaces the current screen in the back stack
                                        popUpTo("meal/$currentMealType") {
                                            inclusive = true
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add to meal",
                                    tint = Color(0xFF2196F3)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealTypeSelector(
    currentMealType: String,
    onMealTypeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MealTypeButton(
            text = "Breakfast",
            selected = currentMealType == "Breakfast",
            onClick = { onMealTypeSelected("Breakfast") }
        )
        MealTypeButton(
            text = "Lunch",
            selected = currentMealType == "Lunch",
            onClick = { onMealTypeSelected("Lunch") }
        )
        MealTypeButton(
            text = "Dinner",
            selected = currentMealType == "Dinner",
            onClick = { onMealTypeSelected("Dinner") }
        )
    }
}

@Composable
fun MealTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text)
    }
}

