package com.example.notas.domain.usecase

import com.example.notas.domain.model.Note
import com.example.notas.domain.repository.NoteRepository

// Caso de uso: guardar una nota
class AddNoteUseCase(
    private val repo: NoteRepository
) {
    // suspend: puede tardar un poco, porque guarda en la base de datos
    suspend operator fun invoke(note: Note): Boolean {
        // Regla de negocio: una nota sin título no se guarda
        if (note.title.isBlank()) {
            return false
        }

        // Si cumple la regla, se pide al repositorio que la guarde
        repo.insert(note)
        return true
    }
}
