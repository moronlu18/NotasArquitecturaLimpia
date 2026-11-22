package com.example.notas.presentation.navigation

// Rutas de la app, definidas en un único sitio
sealed class NavigationItem(val route: String) {
    object Notes : NavigationItem("notes")

    object Details : NavigationItem("details/{itemId}") {
        fun createRoute(itemId: Int) = "details/$itemId"
    }
}
