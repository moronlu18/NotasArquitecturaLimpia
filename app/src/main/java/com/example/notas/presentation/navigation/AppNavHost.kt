package com.example.notas.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notas.presentation.detail.DetailScreen
import com.example.notas.presentation.notes.NotesScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Notes.route
    ) {
        // Lista de notas
        composable(NavigationItem.Notes.route) {
            NotesScreen(
                onNoteClick = { id ->
                    navController.navigate(NavigationItem.Details.createRoute(id))
                }
            )
        }

        // Detalle de una nota: recibe su id por la ruta
        composable(
            route = NavigationItem.Details.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) {
            // DetailViewModel lee el id de su SavedStateHandle
            DetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
