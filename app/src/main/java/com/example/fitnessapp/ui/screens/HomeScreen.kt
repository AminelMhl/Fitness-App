package com.example.fitnessapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
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
    val bmr = viewModel.calculateBMR(userState.weight, userState.height, userState.age, userState.isMale)
    val progress = viewModel.getProgress(userState.caloriesConsumed, bmr).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar with Date & Edit Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = viewModel.getCurrentDate(), fontSize = 20.sp)
            IconButton(onClick = { viewModel.editUserData() }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Data")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calorie Budget Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Calorie Budget", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("BMR: ${bmr.toInt()} kcal")
                    Text("Consumed: ${userState.caloriesConsumed.toInt()} kcal")
                    Text("Remaining: ${(bmr - userState.caloriesConsumed).toInt()} kcal")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Meal Summary Row (BF, L, D)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(
                text = "Break Fast",
                icon = Icons.Default.WbTwilight,
                onClick = { navController.navigate("food") }
            )
            ActionButton(
                text = "Lunch",
                icon = Icons.Default.WbSunny,
                onClick = { navController.navigate("food") }
            )
            ActionButton(
                text = "Dinner",
                icon = Icons.Default.Nightlight,
                onClick = { navController.navigate("food") }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Quick Access Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(
                text = "Exercise",
                icon = Icons.Default.FitnessCenter,
                onClick = { navController.navigate("exercise") }
            )
        }
    }
}

@Composable
fun MealBox(label: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color(0xFFE0E0E0), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 16.sp)
    }
}

@Composable
fun ActionButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(40.dp))
        Text(text)
    }
}
