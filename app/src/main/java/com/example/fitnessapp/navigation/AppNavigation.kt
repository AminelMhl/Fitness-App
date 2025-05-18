package com.example.fitnessapp.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapp.ui.screens.CategoryDetailScreen
import com.example.fitnessapp.ui.screens.EditUserInfoScreen
import com.example.fitnessapp.ui.screens.ExerciseDetailScreen
import com.example.fitnessapp.ui.screens.HomeScreen
import com.example.fitnessapp.ui.screens.ExercisesScreen
import com.example.fitnessapp.ui.screens.FoodScreen
import com.example.fitnessapp.ui.screens.MealDetailScreen
import com.example.fitnessapp.ui.screens.UserInfoScreen
import com.example.fitnessapp.viewmodel.HomeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
        LocalContext.current.applicationContext as Application
    ))
    val userState by homeViewModel.user.collectAsState()
    val startDestination = remember(userState) {
        if (userState.age == 0 || userState.height == 0f || userState.weight == 0f) {
            "userinfo"
        } else {
            "home"
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("userinfo") {
            UserInfoScreen(navController, homeViewModel)
        }
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
        composable("meal/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "Breakfast"
            MealDetailScreen(navController = navController, mealType = type)
        }
        composable("category/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            CategoryDetailScreen(categoryName = name, navController = navController)
        }
        composable("edituser") {
            EditUserInfoScreen(navController, homeViewModel)
        }
    }
}

