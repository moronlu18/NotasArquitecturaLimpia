package com.example.notas.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notas.domain.model.Note
import com.example.notas.domain.usecase.GetNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteById: GetNoteByIdUseCase
) : ViewModel() {

    // Navigation copia los argumentos de la ruta en el SavedStateHandle
    private val itemId: Int = checkNotNull(savedStateHandle.get<Int>("itemId"))

    // Estado de la pantalla: la nota que se muestra
    var note by mutableStateOf<Note?>(null)
        private set

    init {
        viewModelScope.launch {
            getNoteById(itemId).collect { nota -> note = nota }
        }
    }
}
