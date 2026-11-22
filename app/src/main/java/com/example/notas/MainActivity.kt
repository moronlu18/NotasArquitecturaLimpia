package com.example.notas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notas.presentation.navigation.AppNavHost
import com.example.notas.presentation.theme.NotasTheme
import dagger.hilt.android.AndroidEntryPoint

// La actividad solo muestra la navegación: todo lo demás está en sus capas
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotasTheme {
                AppNavHost()
            }
        }
    }
}
