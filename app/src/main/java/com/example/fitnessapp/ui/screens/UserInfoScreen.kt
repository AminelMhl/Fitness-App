package com.example.fitnessapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnessapp.viewmodel.HomeViewModel

@Composable
fun UserInfoScreen(navController: NavController, viewModel: HomeViewModel) {
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var goal by remember { mutableStateOf("Maintain") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tell us about you", style = MaterialTheme.typography.headlineSmall)

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
                    navController.navigate("home") {
                        popUpTo("userinfo") { inclusive = true }
                    }
                } else {
                    // Show error message
                }
            },
            enabled = age.isNotBlank() && weight.isNotBlank() && height.isNotBlank()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun DropdownMenuGoal(goal: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Lose", "Maintain", "Gain")

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(goal)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    onSelect(it)
                    expanded = false
                })
            }
        }
    }
}
