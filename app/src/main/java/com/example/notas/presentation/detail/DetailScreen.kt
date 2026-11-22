package com.example.notas.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notas.domain.model.Note

/**
 * Pantalla stateful: obtiene el ViewModel (que ya trae la nota del repositorio)
 * y delega el dibujo a [DetailScreenContent].
 */
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    DetailScreenContent(
        note = viewModel.note,
        onBack = onBack,
        modifier = modifier
    )
}

/**
 * Pantalla stateless: recibe el estado (la nota) y el evento (volver atrás).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    note: Note?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(note?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (note == null) {
                Text("Cargando…")
            } else {
                Text(note.content, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
