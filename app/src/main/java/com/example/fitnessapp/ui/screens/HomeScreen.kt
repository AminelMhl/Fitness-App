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

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val userState = viewModel.user.collectAsState().value
    val bmr = viewModel.calculateBMR(
        userState.weight,
        userState.height,
        userState.age,
        userState.isMale
    )
    val consumed = userState.caloriesConsumed
    val progress = (consumed / bmr).coerceIn(0f, 1f)

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
                .background(Color(0xFFCB8400), RoundedCornerShape(12.dp))
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
                .background(Color(0xFFD2D2D2), RoundedCornerShape(20.dp))
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
                        .background(Color(0xFFCB8400), RoundedCornerShape(20.dp))
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
            text = "Calory Budget",
            fontSize = 20.sp,
            color = Color(0xFFCB8400)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Exercise Button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFCB8400), RoundedCornerShape(12.dp))
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
            Text("Exercise", fontSize = 16.sp, color = Color(0xFFCB8400))
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
            color = Color(0xFFCB8400),
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
                color = Color(0xFFCB8400),
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun MealIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, desc: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = desc, tint = Color.White, modifier = Modifier.size(32.dp))
    }
}
