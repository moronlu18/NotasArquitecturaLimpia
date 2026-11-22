package com.example.notas.presentation.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notas.domain.model.Note
import com.example.notas.presentation.components.NoteCard

/**
 * Pantalla stateful: es la única que conoce el ViewModel y conserva el estado
 * de la interfaz. Recoge todo y se lo pasa a [NotesScreenContent].
 */
@Composable
fun NotesScreen(
    onNoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel()
) {
    // Lo que el usuario está escribiendo: estado de la propia pantalla
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

    NotesScreenContent(
        notes = viewModel.notes,
        error = viewModel.error,
        title = title,
        content = content,
        onTitleChange = { title = it },
        onContentChange = { content = it },
        onAddNote = { noteTitle, noteContent ->
            // El ViewModel decide si se guarda; si es así se limpia elel formulario
            viewModel.onAddNote(noteTitle, noteContent) {
                title = ""
                content = ""
            }
        },
        onNoteClick = onNoteClick,
        modifier = modifier
    )
}

/**
 * Pantalla stateless: solo recibe el estado y los eventos, así que se puede
 * previsualizar y probar sin ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreenContent(
    notes: List<Note>,
    error: String?,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onAddNote: (String, String) -> Unit,
    onNoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Mis notas") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth()
            )

            // El ViewModel decide el mensaje; la pantalla solo lo muestra
            error?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Button(
                onClick = { onAddNote(title, content) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Guardar")
            }

            Spacer(Modifier.height(16.dp))

            if (notes.isEmpty()) {
                Text(
                    text = "Todavía no hay notas",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(note = note, onClick = { onNoteClick(note.id) })
                }
            }
        }
    }
}
