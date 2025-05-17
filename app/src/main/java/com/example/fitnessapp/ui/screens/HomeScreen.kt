package com.example.fitnessapp.ui.screens

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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.font.FontWeight
import com.example.fitnessapp.ui.components.AppScaffold

data class MacroNutrient(val name: String, val value: Float, val color: Color)

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    AppScaffold{
        val userState = viewModel.user.collectAsState().value
        val bmr = viewModel.calculateBMR(
            userState.weight,
            userState.height,
            userState.age,
            userState.isMale
        )
        val consumed = userState.caloriesConsumed
        val progress = (consumed / bmr).coerceIn(0f, 1f)

        val macros = listOf(
            MacroNutrient("Protein", 80f, Color(0xFFFFEB3B)),  // 80g
            MacroNutrient("Carbs", 200f, Color(0xFFF44336)),   // 200g
            MacroNutrient("Fat", 70f, Color(0xFF009688))       // 70g
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00BCD4), RoundedCornerShape(12.dp))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = viewModel.getCurrentDate(), color = Color.White, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Calorie Budget Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB6B6B6), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CalorieCircle(bmr = bmr, consumed = consumed)

                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(Color(0xFF2196F3), RoundedCornerShape(20.dp))
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        MealIcon(Icons.Default.WbTwilight, "Breakfast") {
                            navController.navigate("food")
                        }
                        MealIcon(Icons.Default.WbSunny, "Lunch") {
                            navController.navigate("food")
                        }
                        MealIcon(Icons.Default.Nightlight, "Dinner") {
                            navController.navigate("food")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Calory Budget: ${bmr.toInt()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color=Color(0xFF1A237E)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Exercise Button
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
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text("Exercise", fontSize = 18.sp, color = Color(0xFF1A237E))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Macro Breakdown
            Text(
                text = "Macronutrient Breakdown",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                MacroPieChart(macros = macros, modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
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
                        Text(it.name, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CalorieCircle(bmr: Float, consumed: Float) {
    val progress = (consumed / bmr).coerceIn(0f, 1f)

    Box(
        modifier = Modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 24.dp,
            color = Color(0xFF2196F3),
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${(bmr - consumed).toInt()}",
                color = Color(0xFF2196F3),
                fontSize = 22.sp
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
