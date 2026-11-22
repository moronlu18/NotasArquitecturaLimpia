package com.example.notas.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notas.domain.model.Note
import com.example.notas.domain.usecase.AddNoteUseCase
import com.example.notas.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// @HiltViewModel e @Inject permiten que Hilt cree el ViewModel
// y le pase los casos de uso (ver di/UseCaseModule.kt)
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotes: GetNotesUseCase,
    private val addNote: AddNoteUseCase
) : ViewModel() {

    // Estado que observa la pantalla: la lista de notas
    var notes by mutableStateOf<List<Note>>(emptyList())
        private set

    // Lógica de presentación: el texto de error que se muestra en la pantalla
    var error by mutableStateOf<String?>(null)
        private set

    init {
        // Al crearse, el ViewModel pide las notas al caso de uso
        // y actualiza la lista cada vez que cambian
        viewModelScope.launch {
            getNotes().collect { lista -> notes = lista }
        }
    }

    // La pantalla avisa al ViewModel cuando el usuario añade una nota.
    // onSaved se llama solo si la nota se ha guardado (para vaciar el formulario).
    fun onAddNote(title: String, content: String, onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            val saved = addNote(Note(id = 0, title = title, content = content))
            if (saved) {
                error = null
                onSaved()
            } else {
                error = "El título no puede estar vacío"
            }
        }
    }
}
