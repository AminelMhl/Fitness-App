package com.example.fitnessapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnessapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserInfoScreen(navController: NavController, viewModel: HomeViewModel) {
    // Get current user data to pre-fill fields
    val userState = viewModel.user.collectAsState().value

    // State variables pre-filled with current user data
    var age by remember { mutableStateOf(userState.age.toString()) }
    var weight by remember { mutableStateOf(userState.weight.toString()) }
    var height by remember { mutableStateOf(userState.height.toString()) }
    var isMale by remember { mutableStateOf(userState.isMale) }
    var goal by remember { mutableStateOf(userState.goal) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row {
                Text("Gender: ")
                Row(Modifier.padding(horizontal = 8.dp)) {
                    RadioButton(selected = isMale, onClick = { isMale = true })
                    Text("Male")
                }
                Row(Modifier.padding(horizontal = 8.dp)) {
                    RadioButton(selected = !isMale, onClick = { isMale = false })
                    Text("Female")
                }
            }

            Row {
                Text("Goal: ")
                DropdownMenuGoal(goal = goal, onSelect = { goal = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (age.isNotBlank() && weight.isNotBlank() && height.isNotBlank()) {
                        viewModel.saveUserData(
                            age.toIntOrNull() ?: 0,
                            weight.toFloatOrNull() ?: 0f,
                            height.toFloatOrNull() ?: 0f,
                            isMale,
                            goal
                        )
                        // Explicitly refresh data before navigation
                        viewModel.refreshNutritionData()
                        // Navigate back to home screen
                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }
                },
                enabled = age.isNotBlank() && weight.isNotBlank() && height.isNotBlank()
            ) {
                Text("Save Changes")
            }
        }
    }
}