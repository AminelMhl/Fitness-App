package com.example.fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnessapp.R

data class Exercise(
    val name: String,
    val description: String,
    val imageResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(navController: NavController, bodyPartName: String) {
    val exercises = getExercisesForBodyPart(bodyPartName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(bodyPartName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(exercises.size) { index ->
                val exercise = exercises[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Image(
                            painter = painterResource(id = exercise.imageResId),
                            contentDescription = exercise.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = exercise.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

fun getExercisesForBodyPart(bodyPart: String): List<Exercise> {
    return when (bodyPart.lowercase()) {
        "chest" -> listOf(
            Exercise("Push-Up", "A bodyweight exercise to target chest, shoulders, and triceps.", R.drawable.push_ups),
            Exercise("Bench Press", "A classic weightlifting chest exercise.", R.drawable.barbell_bench_press),
            Exercise("Incline Bench Press", "A barbell press to target upper chest.", R.drawable.incline_barbell_bench_press)
        )
        "back" -> listOf(
            Exercise("Pull-Up", "Great for building upper back strength.", R.drawable.wide_grip_pulldown),
            Exercise("T-Bar Rows", "A compound lift for back.", R.drawable.t_bar_rows),
            Exercise("Bent Over Row", "Targets the Traps.", R.drawable.barbell_shrug)
        )
        "legs" -> listOf(
            Exercise("Squat", "A fundamental movement for leg development.", R.drawable.squat),
            Exercise("Leg Extension", "Targets quads, glutes, and hamstrings.", R.drawable.leg_extension),
            Exercise("Seated Calf Raise", "Targets the calfs.", R.drawable.seated_calf_raise)
        )
        "biceps" -> listOf(
            Exercise("Bicep Curl", "Isolates and trains the bicep's short head.", R.drawable.barbell_curl),
            Exercise("Incline Dumbbell Curl", "Targets bicep's long head.", R.drawable.incline_dumbbell_curl),
            Exercise("Hammer Curl", "Works the biceps and forearms.", R.drawable.hammer_curl)
        )
        "triceps" -> listOf(
            Exercise("Tricep Dips", "Great for tricep development.", R.drawable.parallel_dip_bar),
            Exercise("Skull Crushers", "Isolates the triceps.", R.drawable.lying_triceps_extension),
            Exercise("Tricep Kickback", "Targets the triceps.", R.drawable.kickback)
        )
        "shoulders" -> listOf(
            Exercise("Shoulder Press", "Overhead pressing for shoulder strength.", R.drawable.dumbbell_shoulder_press),
            Exercise("Lateral Raise", "Isolates side delts.", R.drawable.dumbbell_lateral_raise),
            Exercise("Front Raise", "Targets the front deltoids.", R.drawable.barbell_front_raise)
        )
        "abs" -> listOf(
            Exercise("Crunches", "Targets the upper abs.", R.drawable.crunch),
            Exercise("Plank", "Great for core endurance.", R.drawable.plank),
            Exercise("Oblique Crunch", "Targets the obliques for wider waist.", R.drawable.oblique_crunch)
        )
        else -> emptyList()
    }
}
