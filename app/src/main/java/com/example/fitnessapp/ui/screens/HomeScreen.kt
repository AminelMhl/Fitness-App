package com.example.fitnessapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.viewmodel.HomeViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.font.FontWeight
import com.example.fitnessapp.ui.components.AppScaffold

data class MacroNutrient(val name: String, val value: Float, val color: Color)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    viewModel.refreshNutritionData()

    AppScaffold {
        val userState = viewModel.user.collectAsState().value
        val caloriesBudget = userState.caloriesBudget
        val consumed = userState.caloriesConsumed
        val progress = (consumed / caloriesBudget).coerceIn(0f, 1f)

        val macros = listOf(
            MacroNutrient("Protein", userState.proteinGrams, Color(0xFFFFEB3B)),
            MacroNutrient("Carbs", userState.carbsGrams, Color(0xFFF44336)),
            MacroNutrient("Fat", userState.fatGrams, Color(0xFF009688))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date Header - responsive width
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00BCD4), RoundedCornerShape(12.dp))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = viewModel.getCurrentDate(),
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    IconButton(
                        onClick = { navController.navigate("edituser") },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calorie Budget Section - responsive sizing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB6B6B6), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                BoxWithConstraints {
                    val circleSize = if (maxWidth > 600.dp) 200.dp else
                        if (maxWidth > 400.dp) 160.dp else 120.dp

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CalorieCircle(
                            budget = caloriesBudget,
                            consumed = consumed,
                            modifier = Modifier.size(circleSize)
                        )

                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.height(circleSize)
                        ) {
                            MealIcon(Icons.Default.WbTwilight, "Breakfast") {
                                navController.navigate("meal/Breakfast")
                            }
                            MealIcon(Icons.Default.WbSunny, "Lunch") {
                                navController.navigate("meal/Lunch")
                            }
                            MealIcon(Icons.Default.Nightlight, "Dinner") {
                                navController.navigate("meal/Dinner")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Display calorie budget with goal - responsive text
            Text(
                text = "Calorie Budget: ${caloriesBudget.toInt()} (${userState.goal})",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Exercise Button - responsive width
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00BCD4), RoundedCornerShape(12.dp))
                        .clickable { navController.navigate("exercise") }
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = "Exercise",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    "Exercise",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = Color(0xFF1A237E)
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Macro Breakdown - responsive text
            Text(
                text = "Macronutrient Breakdown",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Responsive chart size based on screen width
            BoxWithConstraints {
                val chartSize = if (maxWidth > 600.dp) 220.dp else
                    if (maxWidth > 400.dp) 180.dp else 140.dp

                Box(
                    modifier = Modifier
                        .size(chartSize)
                        .background(Color.White, shape = CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    MacroPieChart(macros = macros, modifier = Modifier.fillMaxSize())
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend - responsive row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                macros.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(it.color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            it.name,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CalorieCircle(budget: Float, consumed: Float, modifier: Modifier = Modifier) {
    val progress = (consumed / budget).coerceIn(0f, 1f)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 20.dp,
            color = Color(0xFF2196F3),
            modifier = Modifier.fillMaxSize()
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize(0.6f)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${(budget - consumed).toInt()}",
                color = Color(0xFF2196F3),
                fontSize = if (maxWidth > 100.dp) 22.sp else 16.sp
            )
        }
    }
}
@Composable
fun MealIcon(icon: ImageVector, desc: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = desc, tint = Color.White, modifier = Modifier.size(32.dp))
    }
}

@Composable
fun MacroPieChart(macros: List<MacroNutrient>, modifier: Modifier = Modifier) {
    val total = macros.sumOf { it.value.toDouble() }.toFloat().coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        var startAngle = -90f
        macros.forEach { macro ->
            val sweepAngle = (macro.value / total) * 360f
            drawArc(
                color = macro.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}