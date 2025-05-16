package com.example.fitnessapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapp.ui.screens.ExerciseDetailScreen
import com.example.fitnessapp.ui.screens.HomeScreen
import com.example.fitnessapp.ui.screens.ExercisesScreen
import com.example.fitnessapp.ui.screens.FoodScreen
import com.example.fitnessapp.viewmodel.HomeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, homeViewModel)
        }
        composable("exercise") {
            ExercisesScreen(navController)
        }
        composable("bodyPart/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            ExerciseDetailScreen(
                navController,
                bodyPartName = name
            )
        }
        composable("food") {
            FoodScreen(navController)
        }
    }
}
