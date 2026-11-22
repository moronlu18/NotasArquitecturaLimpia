package com.example.notas.domain.usecase

import com.example.notas.domain.repository.NoteRepository

// Caso de uso: obtener las notas
class GetNotesUseCase(
    private val repo: NoteRepository
) {
    // No tiene reglas: solo pide las notas al repositorio
    operator fun invoke() = repo.getNotes()
}
