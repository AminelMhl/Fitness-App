package com.example.fitnessapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnessapp.model.BodyPart
import com.example.fitnessapp.ui.components.BodyPartCard
import com.example.fitnessapp.R

@Composable
fun ExercisesScreen(navController: NavController) {
    val bodyParts = listOf(
        BodyPart("Chest", R.drawable.chest),
        BodyPart("Back", R.drawable.back),
        BodyPart("Legs", R.drawable.legs),
        BodyPart("Biceps", R.drawable.biceps),
        BodyPart("Triceps", R.drawable.triceps),
        BodyPart("Shoulders", R.drawable.shoulders),
        BodyPart("Abs", R.drawable.abs),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(bodyParts) { part ->
            BodyPartCard(bodyPart = part) {
                navController.navigate("bodyPart/${part.name}")
            }
        }
    }
}
