package com.example.notas.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notas.domain.model.Note
import com.example.notas.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// @HiltViewModel e @Inject permiten que Hilt cree el ViewModel
// y le pase el repositorio (ver di/RepositoryModule.kt).
// Recibe la INTERFAZ NoteRepository, no la implementación.
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    // Estado que observa la pantalla: la lista de notas
    var notes by mutableStateOf<List<Note>>(emptyList())
        private set

    // Lógica de presentación: el texto de error que se muestra en la pantalla
    var error by mutableStateOf<String?>(null)
        private set

    init {
        // Al crearse, el ViewModel pide las notas al repositorio
        // y actualiza la lista cada vez que cambian
        viewModelScope.launch {
            repository.getNotes().collect { lista -> notes = lista }
        }
    }

    // La pantalla avisa al ViewModel cuando el usuario añade una nota.
    // onSaved se llama solo si la nota se ha guardado (para vaciar el formulario).
    fun onAddNote(title: String, content: String, onSaved: () -> Unit = {}) {
        // Lógica de negocio ligera: una nota sin título no se guarda
        if (title.isBlank()) {
            error = "El título no puede estar vacío"
            return
        }

        viewModelScope.launch {
            repository.insert(Note(id = 0, title = title.trim(), content = content.trim()))
            error = null
            onSaved()
        }
    }
}
